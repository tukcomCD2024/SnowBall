import requests
import yaml

TALK_URL = "https://api.d-id.com/talks"

with open('conf.yaml', 'r') as f:
    conf = yaml.safe_load(f)


# D-ID에게 영상 제작을 요청
# 대사, 소스 이미지(s3)를 매개변수로 전해줘야 함.
# talk_id 를 리턴 받아야 함
def upload_image():
    payload = {
        "script": {
            "type": "text",  # 텍스트 형태로 대사를 받음
            "subtitles": "false",
            "provider": {  # 보이스 목소리
                "type": "microsoft",
                "voice_id": "en-US-JennyNeural"
            },
            "ssml": "false",
            "input": "hello my name is junrain"  # 대사
        },
        "config": {
            "fluent": "false",
            "pad_audio": "0.0"
        },
        "source_url": "https://cdn.mhnse.com/news/photo/202206/111561_95843_1910.jpg"  # 소스 이미지
    }
    headers = {
        "accept": "application/json",
        "content-type": "application/json",
        "authorization": "Basic" + " " + conf['D-ID_API_KEY']  # 유저 API 키
    }

    response = requests.post(TALK_URL, json=payload, headers=headers)

    print(response.text)


def download_image():
    # D-ID가 만든 영상을 가져옴

    headers = {
        "accept": "application/json",
        "authorization": "Basic" + " " + conf['D-ID_API_KEY']
    }
    # talks/{talk_id}
    # result_url 에서 영상을 가져옴 -> {talk_id} 를 변수 처리해야 함
    response = requests.get(TALK_URL + "/" + "tlk_2XT89GW1t0R8vImW4WQDg", headers=headers)

    print(response.text)
