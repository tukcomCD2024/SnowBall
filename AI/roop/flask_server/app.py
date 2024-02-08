import os
import subprocess

from flask import Flask, request
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

        face_swap_image_name = file_swap(target_image, source_image, text)
        did = DIdAPI()
        result_url = did.run(face_swap_image_name, text)
        print(result_url)

        return 'uploads 디렉토리 -> 파일 업로드 성공!'


def file_swap(target_image, source_image):
    # 파일 확장자와 현재 시간을 결합하여 고유한 해시 생성
    hash_input = target_image.filename + str(time.time())
    target_image_extension = secure_filename(os.path.splitext(target_image.filename)[1])
    target_image_name = hashlib.sha256(hash_input.encode()).hexdigest()[:4]
    target_image_full_name = target_image_name + "." + target_image_extension

    hash_input = source_image.filename + str(time.time())
    source_image_extension = secure_filename(os.path.splitext(source_image.filename)[1])
    source_image_name = hashlib.sha256(hash_input.encode()).hexdigest()[:4]
    source_image_full_name = source_image_name + "." + source_image_extension

    # 저장할 경로 + 고유한 파일명
    target_image.save('../image/' + target_image_full_name)
    source_image.save('../image/' + source_image_full_name)

    command = [
        "python",
        "../run.py",
        "-s",
        "../image/" + source_image_full_name,
        "-t",
        '../image/' + target_image_full_name,
        "-o",
        "../image/faceswap_image/",
        "--frame-processor",
        "face_swapper"
    ]

    subprocess.run(command)

    face_swap_image_name = source_image_name + "-" + target_image_full_name

    return face_swap_image_name


if __name__ == '__main__':
    app.run(port=5001)
