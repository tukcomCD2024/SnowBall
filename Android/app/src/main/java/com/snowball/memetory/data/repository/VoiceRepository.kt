package com.snowball.memetory.data.repository

import com.snowball.memetory.data.api.GenerateMemeService
import com.snowball.memetory.data.dto.generatememe.voice.request.VoiceIdRequestDto
import com.snowball.memetory.data.dto.generatememe.voice.response.VoiceIdResponseDto

class VoiceRepository(private val generateMemeService: GenerateMemeService) {
    suspend fun extractVoice(request: VoiceIdRequestDto): Result<VoiceIdResponseDto> {
        return try {
            val response = generateMemeService.extractVoice(request)
            if (response.isSuccessful) {
                Result.success(response.body()!!)
            } else {
                Result.failure(RuntimeException("Failed to extract voice ID"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
