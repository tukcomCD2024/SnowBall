package com.snowball.memetory.presentation.ui.locker

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.snowball.memetory.data.repository.LockerRepository
import com.snowball.memetory.domain.model.locker.Meme
import kotlinx.coroutines.launch

class LockerViewModel(private val lockerRepository: LockerRepository): ViewModel() {

    private val _memeList = MutableLiveData<List<Meme>>()
    val memeList: LiveData<List<Meme>> = _memeList

    private val _videoUrls = MutableLiveData<List<String>>()
    val videoUrls: LiveData<List<String>> = _videoUrls  // 외부에서 접근 가능한 LiveData

    fun getAllMeme(page: Int, size: Int) {
        viewModelScope.launch {
            val result = lockerRepository.getAllMeme(page, size)
            result.onSuccess {
                _memeList.postValue(it.data.memeList)
                _videoUrls.postValue(it.data.memeList.map { meme -> meme.s3Url })  // s3Url만 추출하여 저장
                Log.d("LockerViewModel", "getAllMeme: ${it.data.memeList}")
            }.onFailure {
                Log.e("LockerViewModel", "Error: ${it.message}")
            }

        }
    }
}