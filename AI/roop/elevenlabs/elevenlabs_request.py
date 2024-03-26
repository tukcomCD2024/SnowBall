import requests

from config.config import Config

config = Config()


def add_voice(name, description, file_name):
    url = "https://api.elevenlabs.io/v1/voices/add"

    payload = {
        "name": name,
        "description": description,
    }

    files = {
        "files": open(f"./elevenlabs/voice/{file_name}", "rb"),
    }

    headers = {
        "xi-api-key": config.ELEVENLABS_API_KEY,
    }

    response = requests.post(url, data=payload, files=files, headers=headers)
    response_json = response.json()
    voice_id = response_json.get("voice_id")

    return voice_id


def delete_voice(voice_id):
    url = f"https://api.elevenlabs.io/v1/voices/{voice_id}"

    response = requests.request("DELETE", url)

    print(response.text)
