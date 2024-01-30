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
        target_file = request.files['targetFile']
        source_file = request.files['sourceFile']

        # 파일명과 현재 시간을 결합하여 고유한 해시 생성
        hash_input = target_file.filename + str(time.time())
        target_filename = hashlib.sha256(hash_input.encode()).hexdigest() + '_' + secure_filename(target_file.filename)

        hash_input = source_file.filename + str(time.time())
        source_filename = hashlib.sha256(hash_input.encode()).hexdigest() + '_' + secure_filename(source_file.filename)

        # 저장할 경로 + 고유한 파일명
        target_file.save('../image/' + target_filename)
        source_file.save('../image/' + source_filename)
        return 'uploads 디렉토리 -> 파일 업로드 성공!'


if __name__ == '__main__':
    app.run()
