package com.snowball.memetory.presentation.ui.locker

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.snowball.memetory.data.repository.LockerRepository

class LockerViewModelFactory(private val lockerRepository: LockerRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LockerViewModel::class.java)) {
            return LockerViewModel(lockerRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

