FROM python:3.10

ENV PYTHONUNBUFFERED 1
WORKDIR /app
COPY . ./

VOLUME ./image:./roop/image

RUN pip install --upgrade pip

RUN apt-get update
RUN apt-get -y install ffmpeg

RUN pip install -r ./roop/requirements.txt

ENTRYPOINT ["top", "-b"]