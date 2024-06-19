import requests
from config.config import Config


class ShotStackAPI:
    _instance = None

    def __new__(cls):
        if cls._instance is None:
            cls._instance = super().__new__(cls)
            cls._instance.config = Config()  # Config 클래스의 인스턴스를 가져와서 저장
            cls._instance.api_url = "https://api.shotstack.io/stage/render"  # API 엔드포인트 URL
        return cls._instance

    # 서버로 데이터 전송
    def send_timeline_data(self, timeline_data):
        headers = {
            "Content-Type": "application/json",
            "x-api-key": self._instance.config.SHOT_STACK_API_KEY
        }

        # 데이터를 JSON 형식으로 변환하여 POST 요청
        response = requests.post(self._instance.api_url, json=timeline_data, headers=headers)

        # 응답 확인
        print(response.status_code)
        print(response.json())  # 만약 API가 JSON 응답을 반환한다면

        return response.json()["response"]["id"]

    # timeline에 영상을 붙임
    def add_track(self, clips, asset_type, asset_src, start, length):
        # 새로운 track을 생성하고 clips를 추가
        new_track = {"clips": [
            {"asset": {"type": asset_type, "src": asset_src}, "start": start, "length": length, "fit": "contain"}]}
        clips.append(new_track)

    # 결과 불러오기
    def download_file(self, id):
        headers = {
            "Content-Type": "application/json",
            "x-api-key": self._instance.config.SHOT_STACK_API_KEY
        }

        api_url_id = self._instance.api_url + "/" + id

        response = requests.get(api_url_id, headers=headers)

        print(f"Status Code: {response.status_code}")
        print("Response:")
        print(response.json())
