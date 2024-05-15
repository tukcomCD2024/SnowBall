package com.snowball.memetory.data.repository

import com.snowball.memetory.data.api.GenerateMemeService
import com.snowball.memetory.data.dto.ResponseBody
import com.snowball.memetory.data.dto.generatememe.voice.request.VoiceIdRequestDto
import com.snowball.memetory.data.dto.generatememe.voice.response.VoiceIdResponseDto
import com.snowball.memetory.domain.model.voice.VoiceList

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

    suspend fun getVoiceIdList(): Result<ResponseBody<VoiceList>> {
        return try {
            val response = generateMemeService.getVoiceIdList()
            if (response.isSuccessful) {
                Result.success(response.body()!!)
            } else {
                Result.failure(RuntimeException("Failed to Get voice ID List"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
