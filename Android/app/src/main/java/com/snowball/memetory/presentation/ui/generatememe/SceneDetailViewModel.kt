package com.snowball.memetory.presentation.ui.generatememe

import android.net.Uri
import androidx.lifecycle.ViewModel
import com.snowball.memetory.data.dto.generatememe.SceneDetailDto

class SceneDetailViewModel: ViewModel() {

    var selectedImageUri: Uri? = null
    var enteredText: String? = null
    var selectedTemplateId: Int? = null
    val selectedImagesState = mutableMapOf<Int, Boolean>()
    val templateDataMap = mutableMapOf<Int, SceneDetailDto>()

    fun setImageSelected(resourceId: Int, isSelected: Boolean) {
        selectedImagesState[resourceId] = isSelected
    }

    fun isImageSelected(resourceId: Int): Boolean {
        return selectedImagesState[resourceId] ?: false
    }
    fun setTemplateData(resourceId: Int, data: SceneDetailDto) {
        templateDataMap[resourceId] = data
    }

    fun getTemplateData(resourceId: Int): SceneDetailDto? {
        return templateDataMap[resourceId]
    }
}