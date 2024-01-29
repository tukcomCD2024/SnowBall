from typing import List, Optional

# 처리할 이미지 또는 비디오 파일의 경로
source_path: Optional[str] = None

# 대상 이미지 또는 비디오 파일의 경로
target_path: Optional[str] = None

# 처리된 결과를 저장할 파일의 경로
output_path: Optional[str] = None

# 프로그램이 GUI 없이 실행되는지 여부 -> GUI 안쓸꺼니깐 필요 없음
headless: Optional[bool] = None

# 프레임 처리기의 이름 목록
frame_processors: List[str] = []

# 대상 비디오의 FPS를 유지할지 여부 -> 비디오 안씀
keep_fps: Optional[bool] = None

# 임시 프레임을 유지할지 여부 -> 프레임 = 비디오 안씀
keep_frames: Optional[bool] = None

# 대상 비디오의 오디오를 건너뛸지 여부 -> 안써
skip_audio: Optional[bool] = None

# 여러 얼굴이 있는 경우를 처리할지 여부 -> 우린 얼굴 하나뿐 안써
many_faces: Optional[bool] = None

# 참조 얼굴의 위치
reference_face_position: Optional[int] = None

# 참조 프레임의 번호 -> 프레임 안써
reference_frame_number: Optional[int] = None

# 유사한 얼굴로 간주되는 거리
similar_face_distance: Optional[float] = None

# 임시 프레임의 형식 -> 프레임 = 비디오 안써
temp_frame_format: Optional[str] = None

# 임시 프레임의 품질 -> 프레임 = 비디오 안써
temp_frame_quality: Optional[int] = None

# 출력 비디오의 인코더 -> 비디오 안써
output_video_encoder: Optional[str] = None

# 출력 비디오의 품질 -> 비디오 안써
output_video_quality: Optional[int] = None

# 최대 메모리 용량
max_memory: Optional[int] = None

# 실행 프로바이더의 이름 목록
execution_providers: List[str] = []

# 실행에 사용할 스레드 수
execution_threads: Optional[int] = None

# 로그 레벨
log_level: str = 'error'
