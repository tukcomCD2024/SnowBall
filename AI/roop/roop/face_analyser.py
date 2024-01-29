import threading
from typing import Any, Optional, List
import insightface
import numpy

import roop.globals
from roop.typing import Frame, Face

FACE_ANALYSER = None
THREAD_LOCK = threading.Lock()

# 얼굴 분석기 객체를 반환. 없으면 새로 생성
def get_face_analyser() -> Any:
    global FACE_ANALYSER

    with THREAD_LOCK:
        if FACE_ANALYSER is None:
            FACE_ANALYSER = insightface.app.FaceAnalysis(name='buffalo_l', providers=roop.globals.execution_providers)
            FACE_ANALYSER.prepare(ctx_id=0)
    return FACE_ANALYSER

# 얼굴 분석기 객체를 초기화
def clear_face_analyser() -> Any:
    global FACE_ANALYSER

    FACE_ANALYSER = None

# 이미지에서 한 개의 얼굴을 가져온다.
def get_one_face(frame: Frame, position: int = 0) -> Optional[Face]:
    many_faces = get_many_faces(frame)
    if many_faces:
        try:
            return many_faces[position]
        except IndexError:
            return many_faces[-1]
    return None

# 이미지에서 여러 개의 얼굴을 가져온다 -> 여러 개는 필요 없는데;
def get_many_faces(frame: Frame) -> Optional[List[Face]]:
    try:
        return get_face_analyser().get(frame)
    except ValueError:
        return None

# 이미지에서 주어진 얼굴과 유사한 얼굴을 찾는다.
def find_similar_face(frame: Frame, reference_face: Face) -> Optional[Face]:
    many_faces = get_many_faces(frame)
    if many_faces:
        for face in many_faces:
            if hasattr(face, 'normed_embedding') and hasattr(reference_face, 'normed_embedding'):
                distance = numpy.sum(numpy.square(face.normed_embedding - reference_face.normed_embedding))
                if distance < roop.globals.similar_face_distance:
                    return face
    return None
