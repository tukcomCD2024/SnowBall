import threading
import numpy
import opennsfw2
from PIL import Image
from keras import Model

from roop.typing import Frame

PREDICTOR = None  # NSFW 예측기 모델을 저장하는 전역 변수
THREAD_LOCK = threading.Lock()  # 스레드 락 객체
MAX_PROBABILITY = 0.85  # NSFW로 판단할 최대 확률 값

# NSFW 예측기 모델을 가져오는 함수
def get_predictor() -> Model:
    global PREDICTOR

    with THREAD_LOCK:
        if PREDICTOR is None:
            PREDICTOR = opennsfw2.make_open_nsfw_model()
    return PREDICTOR

# NSFW 예측기 모델을 초기화하는 함수
def clear_predictor() -> None:
    global PREDICTOR

    PREDICTOR = None

# 프레임을 NSFW 여부를 예측하는 함수 -> 프레임 들어가면 비디오, 즉 없애도 됨
def predict_frame(target_frame: Frame) -> bool:
    image = Image.fromarray(target_frame)  # 프레임을 이미지로 변환
    image = opennsfw2.preprocess_image(image, opennsfw2.Preprocessing.YAHOO)  # 이미지 전처리
    views = numpy.expand_dims(image, axis=0)  # 차원 추가
    _, probability = get_predictor().predict(views)[0]  # 예측 수행
    return probability > MAX_PROBABILITY  # NSFW로 판단할 확률과 비교하여 결과 반환

# 이미지 파일의 NSFW 여부를 예측하는 함수
def predict_image(target_path: str) -> bool:
    return opennsfw2.predict_image(target_path) > MAX_PROBABILITY  # 이미지를 NSFW로 판단할 확률과 비교하여 결과 반환

# 비디오 파일의 NSFW 여부를 예측하는 함수 -> 비디오 따라서 없어도 됨
def predict_video(target_path: str) -> bool:
    _, probabilities = opennsfw2.predict_video_frames(video_path=target_path, frame_interval=100)  # 비디오 프레임의 NSFW 확률 예측
    return any(probability > MAX_PROBABILITY for probability in probabilities)  # NSFW로 판단할 확률과 비교하여 결과 반환
