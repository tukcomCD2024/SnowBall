import os
from dotenv import load_dotenv

BASE_DIR = os.path.dirname(os.path.dirname(__file__))


class Config:
    _instance = None

    DEBUG = False
    DID_API_KEY = None
    ELEVENLABS_API_KEY = None
    SHOT_STACK_API_KEY = None
    AWS_ACCESS_KEY_ID = None
    AWS_SECRET_ACCESS_KEY = None
    AWS_S3_BUCKET_REGION = None
    AWS_S3_BUCKET_NAME = None

    def __new__(cls):
        if cls._instance is None:
            cls._instance = super().__new__(cls)

            load_dotenv()

            cls._instance.DID_API_KEY = os.environ.get("D-ID_API_KEY")
            cls._instance.ELEVENLABS_API_KEY = os.environ.get("ELEVENLABS_API_KEY")
            cls._instance.SHOT_STACK_API_KEY = os.environ.get("SHOT_STACK_API_KEY")
            cls._instance.AWS_ACCESS_KEY_ID = os.environ.get("AWS_ACCESS_KEY_ID")
            cls._instance.AWS_SECRET_ACCESS_KEY = os.environ.get("AWS_SECRET_ACCESS_KEY")
            cls._instance.AWS_S3_BUCKET_REGION = os.environ.get("AWS_S3_BUCKET_REGION")
            cls._instance.AWS_S3_BUCKET_NAME = os.environ.get("AWS_S3_BUCKET_NAME")
        return cls._instance

# 사용 예시:
# config = Config()
# print(config.ID_API_KEY)
