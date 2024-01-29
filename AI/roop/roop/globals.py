from typing import List, Optional

# 처리할 이미지 또는 비디오 파일의 경로
source_path: Optional[str] = None

# 대상 이미지 또는 비디오 파일의 경로
target_path: Optional[str] = None

# 처리된 결과를 저장할 파일의 경로
output_path: Optional[str] = None

# 프레임 처리기의 이름 목록
frame_processors: List[str] = []

# 참조 얼굴의 위치
reference_face_position: Optional[int] = None

# 유사한 얼굴로 간주되는 거리
similar_face_distance: Optional[float] = None

# 최대 메모리 용량
max_memory: Optional[int] = None

# 실행 프로바이더의 이름 목록
execution_providers: List[str] = []

# 실행에 사용할 스레드 수
execution_threads: Optional[int] = None

# 로그 레벨
log_level: str = 'error'
