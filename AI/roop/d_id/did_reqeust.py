import time

import requests
import os
import yaml

TALK_URL = "https://api.d-id.com/talks"
IMAGE_URL = "https://api.d-id.com/images"
FACESWAP_IAMGE_PATH = "./image/faceswap_image"
CONF_PATH = "./d_id/conf.yaml"

class DIdAPI:
    _instance = None

    # 싱글톤 패턴 적용
    def __new__(cls):
        if cls._instance is None:
            cls._instance = super().__new__(cls)
            with open(os.path.abspath(CONF_PATH), 'r') as f:
                cls._instance.conf = yaml.safe_load(f)
        return cls._instance

    """
    1. face_swap_image 업로드 -> s3_url 리턴 (image 업로드)
    2. s3_url, 대사를 담아서 post 요청 -> talk_id 리턴 (talk 생성)
    3. talk_id를 url끝에 붙여서 get요청 -> result_url 리턴 (생성된 talk 가져오기)
    4. 사용자가 브라우저에 result_url 입력하면 밈 다운로드
    """
    def run(self, face_swap_image_name, text):
        s3_image_url = self.upload_image(face_swap_image_name)
        talk_id = self.upload_scene(s3_image_url, text)

        return talk_id

    # D-ID에게 영상 제작을 요청
    # 대사, 소스 이미지(s3)를 매개변수로 전해줘야 함.
    # talk_id 를 리턴 받아야 함
    def upload_scene(self, s3_image_url, text):
        payload = {
            "script": {
                "type": "text",  # 텍스트 형태로 대사를 받음
                "subtitles": "false",
                "provider": {  # 보이스 목소리
                    "type": "microsoft",
                    "voice_id": "en-US-JennyNeural"
                },
                "ssml": "false",
                "input": text  # 대사
            },
            "config": {
                "fluent": "false",
                "pad_audio": "0.0"
            },
            "source_url": s3_image_url  # 소스 이미지
        }
        headers = {
            "accept": "application/json",
            "content-type": "application/json",
            "authorization": "Basic" + " " + self.conf['D-ID_API_KEY']  # 유저 API 키
        }

        response = requests.post(TALK_URL, json=payload, headers=headers)
        response.raise_for_status()     # 요청 실패 예외 처리
        response_dict = response.json()

        return response_dict["id"]

    def download_scene(self, talk_id):
        # D-ID가 만든 영상을 가져옴

        for i in range(1, 11):
            headers = {
                "accept": "application/json",
                "authorization": "Basic" + " " + self.conf['D-ID_API_KEY']
            }

            # talks/{talk_id}
            # result_url 에서 영상을 가져옴 -> {talk_id} 를 변수 처리해야 함
            response = requests.get(TALK_URL + "/" + talk_id, headers=headers)
            response_dict = response.json()
            result_url = response_dict.get("result_url", None)

            if result_url is not None:
                duration = response_dict["duration"]
                return result_url, duration

            time.sleep(1)
            # 10초가 지나도 값을 못가져 왔을 경우 발생시킬 예외 구현

    # Response 에서 url 을 받아옴
    def upload_image(self, face_swap_image_name):
        # (이미지 이름, open 이미지, 이미지 파일 객체, 파일의 MIME 타입)
        files = {"image": (face_swap_image_name, open(FACESWAP_IAMGE_PATH+"/"+face_swap_image_name, "rb"), "image/jpeg")}
        headers = {
            "accept": "application/json",
            "authorization": "Basic" + " " + self.conf['D-ID_API_KEY']
        }

        response = requests.post(IMAGE_URL, files=files, headers=headers)
        response.raise_for_status()
        response_dict = response.json()

        return response_dict["url"]
