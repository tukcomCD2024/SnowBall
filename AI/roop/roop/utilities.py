import glob
import mimetypes
import os
import platform
import shutil
import ssl
import subprocess
import urllib
from pathlib import Path
from typing import List, Optional
from tqdm import tqdm

import roop.globals

TEMP_DIRECTORY = 'temp'
TEMP_VIDEO_FILE = 'temp.mp4'

# macOS에서 SSL 문제를 해결하기 위한 monkey patch
if platform.system().lower() == 'darwin':
    ssl._create_default_https_context = ssl._create_unverified_context

# FFmpeg을 실행하는 함수
def run_ffmpeg(args: List[str]) -> bool:
    commands = ['ffmpeg', '-hide_banner', '-loglevel', roop.globals.log_level]
    commands.extend(args)
    try:
        subprocess.check_output(commands, stderr=subprocess.STDOUT)
        return True
    except Exception:
        pass
    return False

# 비디오의 프레임 속도를 검출하는 함수 -> 비디오 관련 없애도 됨
def detect_fps(target_path: str) -> float:
    command = ['ffprobe', '-v', 'error', '-select_streams', 'v:0', '-show_entries', 'stream=r_frame_rate', '-of', 'default=noprint_wrappers=1:nokey=1', target_path]
    output = subprocess.check_output(command).decode().strip().split('/')
    try:
        numerator, denominator = map(int, output)
        return numerator / denominator
    except Exception:
        pass
    return 30

# 비디오에서 프레임을 추출하는 함수 -> 없애도 됨
def extract_frames(target_path: str, fps: float = 30) -> bool:
    temp_directory_path = get_temp_directory_path(target_path)
    temp_frame_quality = roop.globals.temp_frame_quality * 31 // 100
    return run_ffmpeg(['-hwaccel', 'auto', '-i', target_path, '-q:v', str(temp_frame_quality), '-pix_fmt', 'rgb24', '-vf', 'fps=' + str(fps), os.path.join(temp_directory_path, '%04d.' + roop.globals.temp_frame_format)])

# 비디오를 생성하는 함수 -> 없애도 됨
def create_video(target_path: str, fps: float = 30) -> bool:
    temp_output_path = get_temp_output_path(target_path)
    temp_directory_path = get_temp_directory_path(target_path)
    output_video_quality = (roop.globals.output_video_quality + 1) * 51 // 100
    commands = ['-hwaccel', 'auto', '-r', str(fps), '-i', os.path.join(temp_directory_path, '%04d.' + roop.globals.temp_frame_format), '-c:v', roop.globals.output_video_encoder]
    if roop.globals.output_video_encoder in ['libx264', 'libx265', 'libvpx']:
        commands.extend(['-crf', str(output_video_quality)])
    if roop.globals.output_video_encoder in ['h264_nvenc', 'hevc_nvenc']:
        commands.extend(['-cq', str(output_video_quality)])
    commands.extend(['-pix_fmt', 'yuv420p', '-vf', 'colorspace=bt709:iall=bt601-6-625:fast=1', '-y', temp_output_path])
    return run_ffmpeg(commands)

# 비디오에서 오디오를 복원하는 함수 -> 없애도 됨
def restore_audio(target_path: str, output_path: str) -> None:
    temp_output_path = get_temp_output_path(target_path)
    done = run_ffmpeg(['-i', temp_output_path, '-i', target_path, '-c:v', 'copy', '-map', '0:v:0', '-map', '1:a:0', '-y', output_path])
    if not done:
        move_temp(target_path, output_path)

# 임시 프레임 경로를 반환하는 함수 -> 비디오에서 사용 없애도 됨
def get_temp_frame_paths(target_path: str) -> List[str]:
    temp_directory_path = get_temp_directory_path(target_path)
    return glob.glob((os.path.join(glob.escape(temp_directory_path), '*.' + roop.globals.temp_frame_format)))

# 임시 디렉토리 경로를 반환하는 함수
def get_temp_directory_path(target_path: str) -> str:
    target_name, _ = os.path.splitext(os.path.basename(target_path))
    target_directory_path = os.path.dirname(target_path)
    return os.path.join(target_directory_path, TEMP_DIRECTORY, target_name)

# 임시 출력 파일 경로를 반환하는 함수
def get_temp_output_path(target_path: str) -> str:
    temp_directory_path = get_temp_directory_path(target_path)
    return os.path.join(temp_directory_path, TEMP_VIDEO_FILE)

# 출력 경로를 정규화하는 함수
def normalize_output_path(source_path: str, target_path: str, output_path: str) -> Optional[str]:
    if source_path and target_path and output_path:
        source_name, _ = os.path.splitext(os.path.basename(source_path))
        target_name, target_extension = os.path.splitext(os.path.basename(target_path))
        if os.path.isdir(output_path):
            return os.path.join(output_path, source_name + '-' + target_name + target_extension)
    return output_path

# 임시 디렉토리를 생성하는 함수
def create_temp(target_path: str) -> None:
    temp_directory_path = get_temp_directory_path(target_path)
    Path(temp_directory_path).mkdir(parents=True, exist_ok=True)

# 임시 파일을 이동시키는 함수
def move_temp(target_path: str, output_path: str) -> None:
    temp_output_path = get_temp_output_path(target_path)
    if os.path.isfile(temp_output_path):
        if os.path.isfile(output_path):
            os.remove(output_path)
        shutil.move(temp_output_path, output_path)

# 임시 디렉토리를 정리하는 함수
def clean_temp(target_path: str) -> None:
    temp_directory_path = get_temp_directory_path(target_path)
    parent_directory_path = os.path.dirname(temp_directory_path)
    if not roop.globals.keep_frames and os.path.isdir(temp_directory_path):
        shutil.rmtree(temp_directory_path)
    if os.path.exists(parent_directory_path) and not os.listdir(parent_directory_path):
        os.rmdir(parent_directory_path)

# 이미지 파일 확장자를 가지고 있는지 확인하는 함수
def has_image_extension(image_path: str) -> bool:
    return image_path.lower().endswith(('png', 'jpg', 'jpeg', 'webp'))

# 파일이 이미지인지 확인하는 함수
def is_image(image_path: str) -> bool:
    if image_path and os.path.isfile(image_path):
        mimetype, _ = mimetypes.guess_type(image_path)
        return bool(mimetype and mimetype.startswith('image/'))
    return False

# 파일이 비디오인지 확인하는 함수
def is_video(video_path: str) -> bool:
    if video_path and os.path.isfile(video_path):
        mimetype, _ = mimetypes.guess_type(video_path)
        return bool(mimetype and mimetype.startswith('video/'))
    return False

# 필요에 따라 파일을 다운로드하는 함수
def conditional_download(download_directory_path: str, urls: List[str]) -> None:
    if not os.path.exists(download_directory_path):
        os.makedirs(download_directory_path)
    for url in urls:
        download_file_path = os.path.join(download_directory_path, os.path.basename(url))
        if not os.path.exists(download_file_path):
            request = urllib.request.urlopen(url)  # type: ignore[attr-defined]
            total = int(request.headers.get('Content-Length', 0))
            with tqdm(total=total, desc='Downloading', unit='B', unit_scale=True, unit_divisor=1024) as progress:
                urllib.request.urlretrieve(url, download_file_path, reporthook=lambda count, block_size, total_size: progress.update(block_size))  # type: ignore[attr-defined]

# 상대 경로를 해결하는 함수
def resolve_relative_path(path: str) -> str:
    return os.path.abspath(os.path.join(os.path.dirname(__file__), path))
