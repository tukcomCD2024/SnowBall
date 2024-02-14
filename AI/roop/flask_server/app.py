import base64
import os
import subprocess
from io import BytesIO

from PIL import Image
from flask import Flask, request, jsonify
from werkzeug.utils import secure_filename
from d_id.did_reqeust import DIdAPI
import hashlib
import time

app = Flask(__name__)


# 파일 업로드 처리
@app.route('/file', methods=['GET', 'POST'])
def upload_file():
    if request.method == 'POST':
        # target 파일이 바탕화면
        target_image = request.files['targetImage']
        source_image = request.files['sourceImage']
        text = request.form['text']

        face_swap_image_name = face_swap(target_image, source_image, text)
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
        talk_id_queue = []

        # 여기에서 데이터를 원하는 대로 처리
        for item in data:
            text_data = item.get('text')
            target_image_number = item.get('target_image')
            source_image_base64 = item.get('source_image')

            # Base64 디코딩 및 처리 예시 (이미지를 파일로 저장하는 경우)
            if source_image_base64:
                image_data = base64.b64decode(source_image_base64)
                image = Image.open(BytesIO(image_data))

                # 현재 시간을 기반으로 한 해시값 생성
                hash_input = str(time.time())
                unique_hash = hashlib.sha256(hash_input.encode()).hexdigest()[:4]

                # 고유한 파일 이름으로 저장 (예: decoded_image_<unique_hash>.jpg)
                image.save(f'../image/{unique_hash}.jpg')

            face_swap_image_name = face_swap(target_image_number, unique_hash)
            talk_id = did.run(face_swap_image_name, text_data)
            talk_id_queue.append(talk_id)
            print(talk_id)

        print(talk_id_queue)
        result_url_queue = []

        for item in talk_id_queue:
            result_url_queue.append(did.download_scene(item))

        print(result_url_queue)

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


if __name__ == '__main__':
    app.run(port=5001)
