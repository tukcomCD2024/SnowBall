from typing import Optional

from roop.typing import Face

# 전역 변수로 사용되는 참조 얼굴
FACE_REFERENCE = None

# 현재 설정된 참조 얼굴을 반환
def get_face_reference() -> Optional[Face]:
    return FACE_REFERENCE

# 새로운 얼굴을 참조 얼굴로 설정
def set_face_reference(face: Face) -> None:
    global FACE_REFERENCE

    FACE_REFERENCE = face

# 참조 얼굴을 초기화
def clear_face_reference() -> None:
    global FACE_REFERENCE

    FACE_REFERENCE = None
