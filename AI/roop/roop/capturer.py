# 싹다 비디오 이야기인거 보니깐 필요 없을듯
from typing import Optional
import cv2

from roop.typing import Frame

# 비디오 파일에서 특정 프레임을 가져옴
def get_video_frame(video_path: str, frame_number: int = 0) -> Optional[Frame]:
    capture = cv2.VideoCapture(video_path)
    frame_total = capture.get(cv2.CAP_PROP_FRAME_COUNT)
    capture.set(cv2.CAP_PROP_POS_FRAMES, min(frame_total, frame_number - 1))
    has_frame, frame = capture.read()
    capture.release()
    if has_frame:
        return frame
    return None

# 비디오 파일의 총 프레임 수
def get_video_frame_total(video_path: str) -> int:
    capture = cv2.VideoCapture(video_path)
    video_frame_total = int(capture.get(cv2.CAP_PROP_FRAME_COUNT))
    capture.release()
    return video_frame_total
