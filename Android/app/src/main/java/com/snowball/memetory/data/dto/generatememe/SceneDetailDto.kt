package com.snowball.memetory.data.dto.generatememe

import android.net.Uri

data class SceneDetailDto(
    val imageUri: Uri?, // 선택된 이미지의 URI
    val text: String?   // 사용자가 입력한 텍스트
)
