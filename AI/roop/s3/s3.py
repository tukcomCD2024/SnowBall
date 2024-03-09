import os

import boto3
import yaml

CONF_PATH = "../s3/conf.yaml"


class S3:
    _instance = None

    # 싱글톤 패턴 적용
    def __new__(cls):
        if cls._instance is None:
            cls._instance = super().__new__(cls)
            with open(os.path.abspath(CONF_PATH), 'r') as f:
                cls._instance.conf = yaml.safe_load(f)
        return cls._instance

    def s3_connection(self):
        '''
        s3 bucket에 연결
        :return: 연결된 s3 객체
        '''
        try:
            s3 = boto3.client(
                service_name='s3',
                region_name=self.conf.AWS_S3_BUCKET_REGION,
                aws_access_key_id=self.conf.AWS_ACCESS_KEY,
                aws_secret_access_key=self.conf.AWS_SECRET_ACCESS_KEY
            )
        except Exception as e:
            print(e)
        else:
            print("s3 bucket connected!")
            return s3

