package com.snowball.memetory.data.repository

import com.snowball.memetory.data.api.LockerService
import com.snowball.memetory.data.dto.ResponseBody
import com.snowball.memetory.data.dto.locker.request.GenerateMemesRequestDto
import com.snowball.memetory.data.dto.locker.response.GetAllMemeResponseDto

class LockerRepository(private val lockerService: LockerService) {
    suspend fun getAllMeme(page: Int, size: Int): Result<ResponseBody<GetAllMemeResponseDto>> {
        return try {
            val response = lockerService.getAllMeme(page, size)
            if (response.isSuccessful) {
                Result.success(response.body()!!)
            } else {
                Result.failure(RuntimeException("Failed to load memes"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun generateMemes(request: GenerateMemesRequestDto): Result<ResponseBody<Unit>> {
        return try {
            val response = lockerService.generateMemes(request)
            if (response.isSuccessful) {
                Result.success(response.body()!!)
            } else {
                Result.failure(RuntimeException("Failed to 밈's 생성"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}