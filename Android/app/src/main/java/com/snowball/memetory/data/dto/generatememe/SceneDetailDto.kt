package com.snowball.memetory.data.dto.generatememe

import android.net.Uri

data class SceneDetailDto(
    var sourceImage: String, //S3 URL
    var targetImage: Int, // 1,2 (순서)
    var text: String, // 대사
    var voiceId: String
)