import threading
import opennsfw2
from keras import Model

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


# 이미지 파일의 NSFW 여부를 예측하는 함수
def predict_image(target_path: str) -> bool:
    return opennsfw2.predict_image(target_path) > MAX_PROBABILITY  # 이미지를 NSFW로 판단할 확률과 비교하여 결과 반환
