package com.snowball.memetory.presentation.ui.generatememe.voice
import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.snowball.memetory.data.api.GenerateMemeService
import com.snowball.memetory.data.dto.generatememe.voice.request.VoiceIdRequestDto
import com.snowball.memetory.data.repository.VoiceRepository
import com.snowball.memetory.domain.model.voice.Voice
import com.snowball.memetory.util.FileUtil
import com.snowball.memetory.util.S3Util
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext
import kotlinx.coroutines.Dispatchers
import java.io.File

class VoiceViewModel(private val voiceRepository: VoiceRepository) : ViewModel() {
    private val _selectedVoiceId = MutableLiveData<String>()
    val selectedVoiceId: LiveData<String> = _selectedVoiceId
//    private val _voices = MutableLiveData<List<Voice>>()
//    val voices: LiveData<List<Voice>> = _voices
    private val _voices = MutableLiveData<Voice>()
    val voices: LiveData<Voice> = _voices
    fun selectVoice(voice: Voice) {
        _selectedVoiceId.value = voice.voice_id
    }


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
            Log.e("VoiceViewModel", "$result")
            result.onSuccess { voiceResponse ->
                // 성공 처리 로직
                Log.d("VoiceViewModel", "Voice ID extracted: ${voiceResponse.voiceId}")
                getVoiceIdList()
            }.onFailure { throwable ->
                // 실패 처리 로직
                Log.e("VoiceViewModel", "Error: ${throwable.message}")
            }
        }
    }

    fun getVoiceIdList() {
        viewModelScope.launch {
            val result = voiceRepository.getVoiceIdList()
            Log.e("VoiceViewModel", "$result")
            result.onSuccess { voiceResponse ->
                // 성공 처리 로직
//                _voices.postValue(voiceResponse.data.voices)  // 데이터를 LiveData에 업데이트

                _voices.postValue(voiceResponse.data)  // 데이터를 LiveData에 업데이트
                Log.d("VoiceViewModel", "Voice ID extracted: ,${voiceResponse.status}, ${voiceResponse.message}, " +
                        "${voiceResponse.data}")
            }.onFailure { throwable ->
                // 실패 처리 로직
                Log.e("VoiceViewModel", "Error: ${throwable.message}")
            }
        }
    }
}
