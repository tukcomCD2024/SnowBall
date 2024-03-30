import io
import boto3
from config.config import Config


class S3Manager:
    _instance = None

    def __new__(cls):
        if cls._instance is None:
            cls._instance = super().__new__(cls)
            cls._instance._s3 = None
            cls._instance._config = None
        return cls._instance

    def __init__(self):
        if self._s3 is None:
            self._config = Config()
            try:
                self._s3 = boto3.client(
                    service_name='s3',
                    region_name=self._config.AWS_S3_BUCKET_REGION,
                    aws_access_key_id=self._config.AWS_ACCESS_KEY_ID,
                    aws_secret_access_key=self._config.AWS_SECRET_ACCESS_KEY
                )
            except Exception as e:
                print(e)
            else:
                print("s3 bucket connected!")

    def s3_put_object(self, file_obj, access_key):
        try:
            self._s3.upload_fileobj(file_obj, self._config.AWS_S3_BUCKET_NAME, access_key)
        except Exception as e:
            print(e)
            return False
        return "s3 업로드 성공!"

    def s3_get_object(self, object_name):
        try:
            source_image = io.BytesIO()
            self._s3.download_fileobj(self._config.AWS_S3_BUCKET_NAME, object_name, source_image)
            source_image.seek(0)
            return source_image
        except Exception as e:
            print(e)
            return None
