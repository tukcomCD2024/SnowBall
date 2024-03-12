import base64
import subprocess
from io import BytesIO
from urllib.parse import urlparse

from PIL import Image
from flask import Flask, request, jsonify
from d_id.did_reqeust import DIdAPI
import hashlib
import time

from shotstack.shot_stack import ShotStackAPI
from s3 import s3_request

app = Flask(__name__)
s3 = s3_request.s3_connection()


# 파일 업로드 처리
@app.route('/file', methods=['GET', 'POST'])
def upload_file():
    if request.method == 'POST':
        # target 파일이 바탕화면
        target_image = request.files['targetImage']
        source_image = request.files['sourceImage']
        text = request.form['text']

        face_swap_image_name = face_swap(target_image, source_image)
        did = DIdAPI()
        result_url = did.run(face_swap_image_name, text)
        print(result_url)

        return 'uploads 디렉토리 -> 파일 업로드 성공!'


@app.route('/files', methods=['GET', 'POST'])
def process_data():
    try:
        # JSON 데이터를 파싱하여 Python 객체로 변환
        data = request.get_json()
        did = DIdAPI()
        shotstack = ShotStackAPI()
        talk_id_queue = []

        # 여기에서 데이터를 원하는 대로 처리
        for item in data:
            text_data = item.get('text')
            target_image_number = item.get('target_image')
            source_image_s3_url = item.get('source_image')
            file_name = get_file_name_from_url(source_image_s3_url)

            # Base64 디코딩 및 처리 예시 (이미지를 파일로 저장하는 경우)
            if file_name:
                source_image = s3_request.s3_get_object(s3, file_name)

                # 현재 시간을 기반으로 한 해시값 생성
                hash_input = str(time.time())
                unique_hash = hashlib.sha256(hash_input.encode()).hexdigest()[:4]

                # 파일을 저장할 경로 및 파일 이름 생성
                file_path = f"../image/{unique_hash}.jpg"

                # BytesIO 객체에 저장된 데이터를 파일로 저장
                if save_bytes_io_to_file(source_image, file_path):
                    print(f"파일 저장 성공: {file_path}")
                else:
                    print(f"파일 저장 실패: {file_path}")

            face_swap_image_name = face_swap(target_image_number, unique_hash)
            talk_id = did.run(face_swap_image_name, text_data)
            talk_id_queue.append(talk_id)
            print(talk_id)

        print(talk_id_queue)
        result_url_queue = []

        for item in talk_id_queue:
            result_url_queue.append(did.download_scene(item))

        print(result_url_queue)

        # 초기 타임라인 데이터 객체 -> flask 서버에다 복사해두고, 함수 시작할 때 실행
        timeline_data = {
            "timeline": {
                "tracks": []
            },
            "output": {
                "format": "mp4",
                "resolution": "sd"
            }
        }

        start_time = 0

        while result_url_queue:
            item = result_url_queue.pop(0)
            duration = int(item[1])
            print("duration : " + str(duration))
            shotstack.add_track(timeline_data["timeline"]["tracks"], "video", item[0], start_time, duration)
            start_time = start_time + duration

        print(timeline_data)

        shotstack_id = shotstack.send_timeline_data(timeline_data)
        shotstack.download_file(shotstack_id)

        # 처리 결과 응답
        return jsonify({'message': 'Data processed successfully'})

    except Exception as e:
        # 예외 처리
        print('Error:', str(e))
        return jsonify({'error': 'An error occurred during data processing'})


def face_swap(target_image_number, source_image):
    command = [
        "python",
        "../run.py",
        "-s",
        "../image/" + source_image + '.jpg',
        "-t",
        '../target_image/' + target_image_number + '.jpg',
        "-o",
        "../image/faceswap_image/",
        "--frame-processor",
        "face_swapper"
    ]

    subprocess.run(command)

    face_swap_image_name = source_image + "-" + target_image_number + '.jpg'

    return face_swap_image_name


def save_bytes_io_to_file(bytes_io, file_path):
    """
    BytesIO 객체에 저장된 데이터를 파일로 저장합니다.

    :param bytes_io: BytesIO 객체
    :param file_path: 저장할 파일 경로
    :return: 저장에 성공하면 True, 실패하면 False를 반환합니다.
    """
    try:
        with open(file_path, "wb") as file:
            file.write(bytes_io.getvalue())
        return True
    except Exception as e:
        print(e)
        return False


def get_file_name_from_url(url):
    """
    URL에서 파일 이름을 추출합니다.

    :param url: 파일이 위치한 URL
    :return: 추출된 파일 이름 또는 None (URL에서 파일 이름을 추출할 수 없는 경우)
    """
    try:
        parsed_url = urlparse(url)
        # URL 경로를 '/'로 분할하여 파일 이름을 추출
        file_name = parsed_url.path.split('/')[-1]
        return file_name
    except Exception as e:
        print(e)
        return None


if __name__ == '__main__':
    app.run(port=5001)
