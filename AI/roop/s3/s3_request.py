import boto3
import yaml

CONF_PATH = "../s3/conf.yaml"
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
    :param filepath: 파일 위치
    :param access_key: 저장 파일명
    :return: 성공 시 True, 실패 시 False 반환
    """
    try:
        s3.upload_fileobj(file_obj, BUKET, access_key)
    except Exception as e:
        print(e)
        return False
    return "s3 업로드 성공!"


def s3_get_object(s3, object_name, file_name):
    """
    s3 bucket에서 지정 파일 다운로드
    :param s3: 연결된 s3 객체(boto3 client)
    :param object_name: s3에 저장된 object 명
    :param file_name: 저장할 파일 명(path)
    :return: 성공 시 True, 실패 시 False 반환
    """
    try:
        s3.download_file(BUKET, object_name, file_name)
    except Exception as e:
        print(e)
        return False
    return True
