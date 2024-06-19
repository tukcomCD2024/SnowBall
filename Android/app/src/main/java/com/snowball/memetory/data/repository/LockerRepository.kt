package com.snowball.memetory.data.repository

import com.snowball.memetory.data.api.LockerService
import com.snowball.memetory.data.dto.ResponseBody
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
}