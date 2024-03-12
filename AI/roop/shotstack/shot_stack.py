import time

import requests
import json

import yaml


class ShotStackAPI:
    _instance = None

    def __new__(cls):
        if cls._instance is None:
            cls._instance = super().__new__(cls)
        return cls._instance

    def read_yaml(file_path):
        try:
            with open(file_path, 'r') as file:
                yaml_data = yaml.safe_load(file)
            return yaml_data
        except FileNotFoundError:
            print(f"YAML file not found at: {file_path}")
            return None
        except yaml.YAMLError as e:
            print(f"Error parsing YAML in file: {file_path}")
            print(e)
            return None

    # 사용할 YAML 파일 경로
    yaml_file_path = "./shotstack/conf.yaml"
    # API 엔드포인트 URL
    api_url = "https://api.shotstack.io/stage/render"

    # 사용하는 API 키
    api_key = read_yaml(yaml_file_path).get("API_KEY")

    # 서버로 데이터 전송
    def send_timeline_data(self, timeline_data):
        headers = {
            "Content-Type": "application/json",
            "x-api-key": self.api_key
        }

        # 데이터를 JSON 형식으로 변환하여 POST 요청
        response = requests.post(self.api_url, json=timeline_data, headers=headers)

        # 응답 확인
        print(response.status_code)
        print(response.json())  # 만약 API가 JSON 응답을 반환한다면

        return response.json()["response"]["id"]

    # timeline에 영상을 붙임
    def add_track(self, clips, asset_type, asset_src, start, length):
        # 새로운 track을 생성하고 clips를 추가
        new_track = {"clips": [{"asset": {"type": asset_type, "src": asset_src}, "start": start, "length": length}]}
        clips.append(new_track)

    # 결과 불러오기
    def download_file(self, id):
        headers = {
            "Content-Type": "application/json",
            "x-api-key": self.api_key
        }

        api_url_id = self.api_url + "/" + id

        response = requests.get(api_url_id, headers=headers)

        print(f"Status Code: {response.status_code}")
        print("Response:")
        print(response.json())


if __name__ == '__main__':
    # 함수 호출을 통해 객체 전송
    shotstack = ShotStackAPI()
    shotstack.download_file('6e9f93ad-1259-4316-af64-800b68886670')
#
# # 새로운 track을 추가할 때마다 호출
# add_track(timeline_data["timeline"]["tracks"], "video",
#           "https://shotstack-assets.s3.ap-southeast-2.amazonaws.com/footage/shore-overhead.mp4", 0, 19)
# add_track(timeline_data["timeline"]["tracks"], "video",
#           "https://shotstack-assets.s3.ap-southeast-2.amazonaws.com/footage/cliffs-sunset.mp4", 19, 8)
# add_track(timeline_data["timeline"]["tracks"], "video",
#           "https://shotstack-assets.s3.ap-southeast-2.amazonaws.com/footage/tree.mp4", 27, 16)
