package com.snowball.memetory.presentation.ui.generatememe.scenedetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.snowball.memetory.data.repository.GenerateMemeRepository

class SceneDetailViewModelFactory(private val memeRepository: GenerateMemeRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SceneDetailViewModel::class.java)) {
            return SceneDetailViewModel(memeRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}