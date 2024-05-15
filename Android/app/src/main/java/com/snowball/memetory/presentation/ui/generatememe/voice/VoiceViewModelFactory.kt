package com.snowball.memetory.presentation.ui.generatememe.voice

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.snowball.memetory.data.api.GenerateMemeService
import com.snowball.memetory.data.repository.VoiceRepository

class VoiceViewModelFactory(private val voiceRepository: VoiceRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(VoiceViewModel::class.java)) {
            return VoiceViewModel(voiceRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
