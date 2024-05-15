package com.snowball.memetory.presentation.ui.generatememe.voice
import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.snowball.memetory.data.api.GenerateMemeService
import com.snowball.memetory.data.dto.generatememe.voice.request.VoiceIdRequestDto
import com.snowball.memetory.data.repository.VoiceRepository
import com.snowball.memetory.util.FileUtil
import com.snowball.memetory.util.S3Util
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext
import kotlinx.coroutines.Dispatchers
import java.io.File

class VoiceViewModel(private val voiceRepository: VoiceRepository) : ViewModel() {

    fun uploadFileAndHandle(context: Context, uri: Uri) {
        viewModelScope.launch(Dispatchers.IO) {
            val fileName = FileUtil.getFileName(context, uri) ?: "default_audio.m4a"
            val file = FileUtil.createFileFromUri(context, uri, fileName)
            if (file != null) {
                val isSuccess = S3Util(context).uploadAudioFile(fileName, file) // 이 부분 수정
                if (isSuccess) {
                    val voiceInfo = VoiceIdRequestDto(fileName, fileName, fileName)
                    extractVoice(voiceInfo)
                } else {
                    Log.e("VoiceViewModel", "File upload failed")
                }
            } else {
                Log.e("VoiceViewModel", "Error creating file from URI")
            }
        }
    }


    fun extractVoice(request: VoiceIdRequestDto) {
        viewModelScope.launch {
            val result = voiceRepository.extractVoice(request)
            result.onSuccess { voiceResponse ->
                // 성공 처리 로직
                Log.d("VoiceViewModel", "Voice ID extracted: ${voiceResponse.voiceId}")
            }.onFailure { throwable ->
                // 실패 처리 로직
                Log.e("VoiceViewModel", "Error: ${throwable.message}")
            }
        }
    }
}
