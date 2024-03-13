import io

import boto3
import yaml

CONF_PATH = "./s3/conf.yaml"
BUKET = "memetory"


def load_config():
    with open(CONF_PATH, 'r') as file:
        config = yaml.safe_load(file)
    return config


def s3_connection():
    """
    s3 bucket에 연결
    :return: 연결된 s3 객체
    """
    try:
        config = load_config()
        s3 = boto3.client(
            service_name='s3',
            region_name=config['AWS_S3_BUCKET_REGION'],
            aws_access_key_id=config['AWS_ACCESS_KEY'],
            aws_secret_access_key=config['AWS_SECRET_ACCESS_KEY']
        )
    except Exception as e:
        print(e)
    else:
        print("s3 bucket connected!")
        return s3


def s3_put_object(s3, file_obj, access_key):
    """
    s3 bucket에 지정 파일 업로드
    :param s3: 연결된 s3 객체(boto3 client)
    :param file_obj: 파일 객체
    :param access_key: 저장 파일명
    :return: 성공 시 True, 실패 시 False 반환
    """
    try:
        s3.upload_fileobj(file_obj, BUKET, access_key)
    except Exception as e:
        print(e)
        return False
    return "s3 업로드 성공!"


def s3_get_object(s3, object_name):
    """
    S3 버킷에서 파일 객체를 다운로드합니다.

    :param s3: 연결된 S3 클라이언트 (boto3 클라이언트)
    :param object_name: S3 버킷에 저장된 객체의 이름
    :return: 성공 시 다운로드된 파일의 내용을 포함하는 파일 유사 객체, 실패 시 None 반환
    """
    try:
        # 다운로드된 파일을 저장할 메모리 내 파일 유사 객체를 생성합니다.
        source_image = io.BytesIO()

        # S3 버킷에서 파일을 파일 유사 객체로 다운로드합니다.
        s3.download_fileobj(BUKET, object_name, source_image)

        # 파일 유사 객체의 포인터를 파일의 처음으로 되돌립니다.
        source_image.seek(0)

        return source_image
    except Exception as e:
        print(e)
        return None
