from typing import Any

from insightface.app.common import Face
import numpy # 행렬이나 일반적으로 대규모 다차원 배열을 수비게 처리할 수 있도록 지원하는 파이썬의 라이브러리

Face = Face
# numpy의 핵심인 다차원 행렬 자료구조 클래스, 파이썬에서 제공하는 List 자료형과 동일한 출력형태를 갖는다.
# c의 배열의 특성인 연속적인 메모리에 배치 -> 연산의 속도를 향상
Frame = numpy.ndarray[Any, Any]
