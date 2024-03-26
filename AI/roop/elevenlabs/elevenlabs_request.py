import requests
from config.config import Config


class ElevenLabsAPI:
    _instance = None

    def __new__(cls):
        if cls._instance is None:
            cls._instance = super().__new__(cls)
            cls._instance.config = Config()  # Config 클래스의 인스턴스를 가져와서 저장
        return cls._instance

    def add_voice(self, name, description, file_name):
        url = "https://api.elevenlabs.io/v1/voices/add"

        payload = {
            "name": name,
            "description": description,
        }

        files = {
            "files": open(f"./elevenlabs/voice/{file_name}", "rb"),
        }

        headers = {
            "xi-api-key": self.config.ELEVENLABS_API_KEY,
        }

        response = requests.post(url, data=payload, files=files, headers=headers)
        response_json = response.json()
        voice_id = response_json.get("voice_id")

        return voice_id

    def delete_voice(self, voice_id):
        url = f"https://api.elevenlabs.io/v1/voices/{voice_id}"

        response = requests.delete(url, headers={"xi-api-key": self.config.ELEVENLABS_API_KEY})

        print(response.text)
