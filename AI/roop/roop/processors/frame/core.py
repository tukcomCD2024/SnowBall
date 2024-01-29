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
    'pre_check',  # 사전 체크 함수
    'pre_start',  # 사전 시작 함수
    'process_frame',  # 프레임 처리 함수
    'process_image',  # 이미지 처리 함수
    'post_process'  # 후처리 함수
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
