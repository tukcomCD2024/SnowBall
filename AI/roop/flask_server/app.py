from flask import Flask, render_template, request
from werkzeug.utils import secure_filename

app = Flask(__name__)


# 파일 업로드 처리
@app.route('/fileUpload', methods=['GET', 'POST'])
def upload_file():
    if request.method == 'POST':
        # target 파일이 바탕화면
        target_file = request.files['targetFile']
        source_file = request.files['sourceFile']
        # 저장할 경로 + 파일명
        target_file.save('/Users/junrain/Downloads/server/' + secure_filename(target_file.filename))
        source_file.save('/Users/junrain/Downloads/server/' + secure_filename(source_file.filename))
        return 'uploads 디렉토리 -> 파일 업로드 성공!'


if __name__ == '__main__':
    app.run()
