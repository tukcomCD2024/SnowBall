import os
import subprocess

from flask import Flask, render_template, request
from werkzeug.utils import secure_filename
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

        # 파일 확장자와 현재 시간을 결합하여 고유한 해시 생성
        hash_input = target_image.filename + str(time.time())
        target_image_name = hashlib.sha256(hash_input.encode()).hexdigest()[:4] + "." + secure_filename(
            os.path.splitext(target_image.filename)[1])

        hash_input = source_image.filename + str(time.time())
        source_image_name = hashlib.sha256(hash_input.encode()).hexdigest()[:4] + "." + secure_filename(
            os.path.splitext(source_image.filename)[1])

        # 저장할 경로 + 고유한 파일명
        target_image.save('../image/' + target_image_name)
        source_image.save('../image/' + source_image_name)

        command = [
            "python",
            "../run.py",
            "-s",
            "../image/" + source_image_name,
            "-t",
            '../image/' + target_image_name,
            "-o",
            "../image/faceswap_image/",
            "--frame-processor",
            "face_swapper"
        ]

        subprocess.run(command)

        return 'uploads 디렉토리 -> 파일 업로드 성공!'


if __name__ == '__main__':
    app.run(port=5001)
