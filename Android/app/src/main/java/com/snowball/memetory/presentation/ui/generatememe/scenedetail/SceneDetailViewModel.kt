package com.snowball.memetory.presentation.ui.generatememe.scenedetail

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.snowball.memetory.data.dto.generatememe.meme.request.GenerateMemeRequestDto
import com.snowball.memetory.data.dto.generatememe.voice.request.VoiceIdRequestDto
import com.snowball.memetory.data.repository.GenerateMemeRepository
import com.snowball.memetory.domain.model.generatememe.Scene
import com.snowball.memetory.util.FileUtil
import com.snowball.memetory.util.S3Util
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SceneDetailViewModel(private val generateMemeRepository: GenerateMemeRepository) : ViewModel() {
    private val _scenes = MutableLiveData<MutableList<Scene>>(mutableListOf())
    val scenes: LiveData<MutableList<Scene>> = _scenes

    private val _selectedScene = MutableLiveData<Scene>()
    val selectedScene: LiveData<Scene> = _selectedScene

    // 이미지 URI
    private val _selectedImageUri = MutableLiveData<Uri>()
    val selectedImageUri: LiveData<Uri> = _selectedImageUri
////////////////////////////////////////// 밈생성 request
    // 선택된 템플릿 이미지 인덱스를 저장하는 LiveData
    private val _targetImage = MutableLiveData<String>()
    val targetImage: LiveData<String> = _targetImage
    // 사용자가 선택한 이미지 - S3URL형태
    private val _sourceImage = MutableLiveData<String>()
    val sourceImage: LiveData<String> = _sourceImage
    // 사용자가 입력한 대사
    private val _text = MutableLiveData<String>()
    val text: LiveData<String> = _text
    //
    private val _voiceId = MutableLiveData<String>()
    val voiceId: LiveData<String> = _voiceId
///////////////////////////////////////////

    private var isIndexInitiallySet = false
    fun setSelectedImageIndex(index: Int) {
        if (!isIndexInitiallySet) {
            setTargetImage(index.toString())
            isIndexInitiallySet = true
            Log.d("ViewModel", "Initial set of Image Index: $index")
        } else {
            Log.d("ViewModel", "Attempt to reset Image Index ignored")
        }
    }

    fun addOrUpdateSelectedScene() {
        val newScene = Scene(
            voiceId = voiceId.value ?: "",
            text = text.value ?: "",
            targetImage = targetImage.value ?: "",
            sourceImage = sourceImage.value ?: ""
        )
        _selectedScene.value = newScene
        _scenes.value?.add(newScene)
        _scenes.value = _scenes.value // LiveData 업데이트
    }
    fun setTargetImage(index: String) {
        _targetImage.value = index
    }

    fun setImageUri(uri: Uri) {
        _selectedImageUri.value = uri
    }

    fun setImageUrl(url: String) {
        _sourceImage.value = url
    }

    fun setEnteredText(text: String) {
        _text.value = text
    }

    fun setVoiceId(voiceId: String) {
        _voiceId.value = voiceId
    }

    // 모든 LiveData 초기화
    fun resetData() {
//        _selectedImageUri.value = Uri.parse("")
        _targetImage.value = ""  // 혹은 적절한 초기값
        _sourceImage.value = ""  // 혹은 적절한 초기값
        _text.value = ""  // 빈 문자열이나 적절한 초기값
        _voiceId.value = ""  // 혹은 적절한 초기값
    }


    init {
//        _scenes.value = emptyList<>()  // 초기화
    }

    // Scene 선택
    fun selectScene(index: Int) {
        _scenes.value?.let {
            if (index in it.indices) {
                _selectedScene.value = it[index]
            }
        }
    }

    // Scene 데이터 추가 또는 업데이트
    fun addOrUpdateScene(scene: Scene, index: Int? = null) {
        val updatedScenes = _scenes.value?.toMutableList() ?: mutableListOf()
        if (index != null && index in updatedScenes.indices) {
            updatedScenes[index] = scene
        } else {
            updatedScenes.add(scene)
        }
        _scenes.value = updatedScenes
    }
    // Meme 생성
    fun generateMeme() {
        viewModelScope.launch {
            val scenesData = _scenes.value ?: return@launch  // 현재 저장된 Scene 리스트 가져오기
            val requestDto = GenerateMemeRequestDto(scenesData)
            try {
                val response = generateMemeRepository.generateMeme(requestDto)
                if (response.isSuccess) {
                    Log.d("SceneDetailViewModel", "Meme generated successfully")
                } else {
                    Log.e("SceneDetailViewModel", "Failed to generate meme: ${response.exceptionOrNull()}")
                }
            } catch (e: Exception) {
                Log.e("SceneDetailViewModel", "Exception in generating meme: ${e.localizedMessage}")
            }
        }
    }
}