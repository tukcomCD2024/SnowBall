package com.snowball.memetory.data.repository

import com.snowball.memetory.data.api.GenerateMemeService
import com.snowball.memetory.data.dto.ResponseBody
import com.snowball.memetory.data.dto.generatememe.meme.request.GenerateMemeRequestDto

class GenerateMemeRepository(private val generateMemeService: GenerateMemeService) {

    suspend fun generateMeme(request: GenerateMemeRequestDto): Result<ResponseBody<Any>> {
        return try {
            val response = generateMemeService.generateMeme(request)
            if (response.isSuccessful) {
                Result.success(response.body()!!)
            } else {
                Result.failure(RuntimeException("Failed to generate meme"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}