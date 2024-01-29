import os
import sys
import importlib
import psutil
from concurrent.futures import ThreadPoolExecutor, as_completed
from queue import Queue
from types import ModuleType
from typing import Any, List, Callable
from tqdm import tqdm

import roop

# 프레임 처리기 모듈을 저장할 리스트
FRAME_PROCESSORS_MODULES: List[ModuleType] = []
# 프레임 처리기 인터페이스 목록
FRAME_PROCESSORS_INTERFACE = [
    'pre_check', # 사전 체크 함수
    'pre_start', # 사전 시작 함수
    'process_frame', # 프레임 처리 함수
    'process_frames', # 프레임들 처리 함수
    'process_image', # 이미지 처리 함수
    'process_video', # 비디오 처리 함수
    'post_process' # 후처리 함수
]

# 주어진 프레임 처리기를 로드하는 함수
def load_frame_processor_module(frame_processor: str) -> Any:
    try:
        frame_processor_module = importlib.import_module(f'roop.processors.frame.{frame_processor}')
        # 프레임 처리기 모듈의 각 인터페이스 메서드가 있는지 확인
        for method_name in FRAME_PROCESSORS_INTERFACE:
            if not hasattr(frame_processor_module, method_name):
                raise NotImplementedError
    except ModuleNotFoundError:
        sys.exit(f'Frame processor {frame_processor} not found.')
    except NotImplementedError:
        sys.exit(f'Frame processor {frame_processor} not implemented correctly.')
    return frame_processor_module

# 주어진 프레임 처리기 목록에 대한 모듈들을 반환하는 함수
def get_frame_processors_modules(frame_processors: List[str]) -> List[ModuleType]:
    # 주어진 프레임 처리기 목록에 대한 모듈들을 반환하는 함수
    global FRAME_PROCESSORS_MODULES

    if not FRAME_PROCESSORS_MODULES:
        # FRAME_PROCESSORS_MODULES가 비어 있는 경우에만 모듈들을 로드하고 저장
        for frame_processor in frame_processors:
            frame_processor_module = load_frame_processor_module(frame_processor)
            FRAME_PROCESSORS_MODULES.append(frame_processor_module)
    return FRAME_PROCESSORS_MODULES

# 여러 프레임을 병렬로 처리하는 함수
def multi_process_frame(source_path: str, temp_frame_paths: List[str], process_frames: Callable[[str, List[str], Any], None], update: Callable[[], None]) -> None:
    with ThreadPoolExecutor(max_workers=roop.globals.execution_threads) as executor:
        futures = []
        queue = create_queue(temp_frame_paths)
        queue_per_future = max(len(temp_frame_paths) // roop.globals.execution_threads, 1)
        while not queue.empty():
            # 프레임 처리 함수를 비동기적으로 실행
            future = executor.submit(process_frames, source_path, pick_queue(queue, queue_per_future), update)
            futures.append(future)
        for future in as_completed(futures):
            future.result()


def create_queue(temp_frame_paths: List[str]) -> Queue[str]:
    # 임시 프레임 경로를 큐로 생성하는 함수
    queue: Queue[str] = Queue()
    for frame_path in temp_frame_paths:
        queue.put(frame_path)
    return queue


def pick_queue(queue: Queue[str], queue_per_future: int) -> List[str]:
    # 큐에서 일정 개수의 항목을 가져오는 함수
    queues = []
    for _ in range(queue_per_future):
        if not queue.empty():
            queues.append(queue.get())
    return queues


def process_video(source_path: str, frame_paths: list[str], process_frames: Callable[[str, List[str], Any], None]) -> None:
    # 비디오를 처리하는 함수
    progress_bar_format = '{l_bar}{bar}| {n_fmt}/{total_fmt} [{elapsed}<{remaining}, {rate_fmt}{postfix}]'
    total = len(frame_paths)
    with tqdm(total=total, desc='Processing', unit='frame', dynamic_ncols=True, bar_format=progress_bar_format) as progress:
        multi_process_frame(source_path, frame_paths, process_frames, lambda: update_progress(progress))


def update_progress(progress: Any = None) -> None:
    # 진행 사항을 업데이트하는 함수
    process = psutil.Process(os.getpid())
    memory_usage = process.memory_info().rss / 1024 / 1024 / 1024
    progress.set_postfix({
        'memory_usage': '{:.2f}'.format(memory_usage).zfill(5) + 'GB',
        'execution_providers': roop.globals.execution_providers,
        'execution_threads': roop.globals.execution_threads
    })
    progress.refresh()
    progress.update(1)
