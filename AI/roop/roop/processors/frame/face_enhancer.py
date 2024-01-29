from typing import Any, List, Callable  # 다양한 타입과 함수 시그니처를 지원하기 위한 typing 모듈 임포트
import cv2  # OpenCV 라이브러리 임포트
import threading  # 스레딩을 위한 threading 모듈 임포트
from gfpgan.utils import GFPGANer  # 얼굴 향상을 위한 GFPGANer 클래스 임포트

import roop.globals  # ROOP 전역 변수 및 설정을 포함한 모듈 임포트
import roop.processors.frame.core  # 프레임 처리 관련 핵심 모듈 임포트
from roop.core import update_status  # ROOP 코어 모듈에서 상태 업데이트 함수 임포트
from roop.face_analyser import get_many_faces  # 다수의 얼굴을 가져오기 위한 함수 임포트
from roop.typing import Frame, Face  # 프레임과 얼굴 타입 임포트
from roop.utilities import conditional_download, resolve_relative_path, is_image, is_video  # 유틸리티 함수 임포트

FACE_ENHANCER = None  # 얼굴 향상기 객체를 저장하기 위한 전역 변수
THREAD_SEMAPHORE = threading.Semaphore()  # 스레드 세마포어 객체 생성
THREAD_LOCK = threading.Lock()  # 스레드 락 객체 생성
NAME = 'ROOP.FACE-ENHANCER'  # 모듈의 이름


# 얼굴 향상기 객체를 반환하는 함수
def get_face_enhancer() -> Any:
    global FACE_ENHANCER

    with THREAD_LOCK:  # 스레드 락을 사용하여 동시 접근 방지
        if FACE_ENHANCER is None:  # 얼굴 향상기가 없는 경우
            model_path = resolve_relative_path('../models/GFPGANv1.4.pth')  # 모델 경로 지정
            FACE_ENHANCER = GFPGANer(model_path=model_path, upscale=1, device=get_device())  # GFPGANer 객체 생성
    return FACE_ENHANCER  # 얼굴 향상기 객체 반환


# 사용 가능한 실행 프로바이더에 따라 장치를 선택하는 함수
def get_device() -> str:
    if 'CUDAExecutionProvider' in roop.globals.execution_providers:  # CUDAExecutionProvider가 있는 경우
        return 'cuda'  # CUDA 디바이스 선택
    if 'CoreMLExecutionProvider' in roop.globals.execution_providers:  # CoreMLExecutionProvider가 있는 경우
        return 'mps'  # MPS 디바이스 선택
    return 'cpu'  # CPU 디바이스 선택


# 얼굴 향상기 객체를 초기화하는 함수
def clear_face_enhancer() -> None:
    global FACE_ENHANCER

    FACE_ENHANCER = None  # 얼굴 향상기 객체를 None으로 재설정


# 전처리 작업을 수행하는 함수
def pre_check() -> bool:
    download_directory_path = resolve_relative_path('../models')  # 모델 다운로드 경로 지정
    conditional_download(download_directory_path, [
        'https://github.com/TencentARC/GFPGAN/releases/download/v1.3.4/GFPGANv1.4.pth'])  # 필요한 모델 다운로드
    return True  # 전처리 성공을 나타내는 True 반환


# 처리 직전에 필요한 작업을 수행하는 함수
def pre_start() -> bool:
    if not is_image(roop.globals.target_path) and not is_video(roop.globals.target_path):  # 대상이 이미지나 비디오가 아닌 경우
        update_status('Select an image or video for target path.', NAME)  # 상태 업데이트
        return False  # 시작할 수 없음을 나타내는 False 반환
    return True  # 시작 가능을 나타내는 True 반환


# 처리 후 정리 작업을 수행하는 함수
def post_process() -> None:
    clear_face_enhancer()  # 얼굴 향상기 객체 초기화


# 얼굴을 향상시키는 함수
def enhance_face(target_face: Face, temp_frame: Frame) -> Frame:
    start_x, start_y, end_x, end_y = map(int, target_face['bbox'])  # 얼굴 경계 상자 좌표 추출
    padding_x = int((end_x - start_x) * 0.5)  # X축 패딩 계산
    padding_y = int((end_y - start_y) * 0.5)  # Y축 패딩 계산
    start_x = max(0, start_x - padding_x)  # 시작 X 좌표 조정
    start_y = max(0, start_y - padding_y)  # 시작 Y 좌표 조정
    end_x = max(0, end_x + padding_x)  # 끝 X 좌표 조정
    end_y = max(0, end_y + padding_y)  # 끝 Y 좌표 조정
    temp_face = temp_frame[start_y:end_y, start_x:end_x]  # 얼굴 영역 추출
    if temp_face.size:  # 얼굴 영역이 있는 경우
        with THREAD_SEMAPHORE:  # 스레드 세마포어 사용
            _, _, temp_face = get_face_enhancer().enhance(  # 얼굴 향상 진행
                temp_face,
                paste_back=True
            )
        temp_frame[start_y:end_y, start_x:end_x] = temp_face  # 향상된 얼굴 영역을 원본 프레임에 복사
    return temp_frame  # 향상된 프레임 반환


# 단일 프레임을 처리하는 함수
def process_frame(source_face: Face, reference_face: Face, temp_frame: Frame) -> Frame:
    many_faces = get_many_faces(temp_frame)  # 다수의 얼굴을 가져옴
    if many_faces:  # 다수의 얼굴이 있는 경우
        for target_face in many_faces:  # 각 얼굴에 대해
            temp_frame = enhance_face(target_face, temp_frame)  # 얼굴 향상 처리
    return temp_frame  # 처리된 프레임 반환


# 이미지를 처리하는 함수
def process_image(source_path: str, target_path: str, output_path: str) -> None:
    target_frame = cv2.imread(target_path)  # 대상 이미지 로드
    result = process_frame(None, None, target_frame)  # 이미지 프레임 처리
    cv2.imwrite(output_path, result)  # 처리된 이미지 저장
