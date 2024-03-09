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

    def s3_put_object(s3, bucket, filepath, access_key):
        '''
        s3 bucket에 지정 파일 업로드
        :param s3: 연결된 s3 객체(boto3 client)
        :param bucket: 버킷명
        :param filepath: 파일 위치
        :param access_key: 저장 파일명
        :return: 성공 시 True, 실패 시 False 반환
        '''
        try:
            s3.upload_file(filepath, bucket, access_key)
        except Exception as e:
            print(e)
            return False
        return "s3 업로드 성공!"
    