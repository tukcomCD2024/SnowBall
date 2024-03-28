from flask import Flask, request, Response

from tasks import generate_meme

app = Flask(__name__)


@app.route('/files', methods=['POST'])
def process_data():
    data = request.get_json()
    generate_meme.delay(data)
    return Response(status=201)


# @app.route("/voice", methods=['POST'])
# def add_voice():
#     voice_file = request.files['voice_file']
#     name = request.form['name']
#     description = request.form['description']
#     file_name = voice_file.filename
#
#     save_bytes_io_to_file(voice_file, f"elevenlabs/voice/{file_name}")
#
#     voice_id = elevenlabs.add_voice(name, description, file_name)
#     return jsonify({"message": "Voice added successfully", "data": voice_id}), 200


if __name__ == '__main__':
    app.run(host="0.0.0.0", port=5001)
