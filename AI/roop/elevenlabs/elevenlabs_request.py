import requests
import yaml


CONF_PATH = "./elevenlabs/conf.yaml"


def load_config():
    with open(CONF_PATH, 'r') as file:
        config = yaml.safe_load(file)
        return config


def add_voice(name, description, file_name):
    config = load_config()

    url = "https://api.elevenlabs.io/v1/voices/add"

    payload = {
        "name": name,
        "description": description,
    }

    files = {
        "files": open(f"./elevenlabs/voice/{file_name}", "rb"),
    }

    headers = {
        "xi-api-key": config["api_key"],
    }

    response = requests.post(url, data=payload, files=files, headers=headers)
    response_json = response.json()
    voice_id = response_json.get("voice_id")

    return voice_id


def delete_voice(voice_id):
    url = f"https://api.elevenlabs.io/v1/voices/{voice_id}"

    response = requests.request("DELETE", url)

    print(response.text)
