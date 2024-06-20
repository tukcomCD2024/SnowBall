package com.snowball.memetory.presentation.ui.generatememe

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.Navigation.findNavController
import com.snowball.memetory.R

class GenerateMemeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        val selectedTemplateIndex = intent.getIntExtra("selectedTemplateIndex", 0) // Default 값을 0으로 설정
        // 데이터 저장
        val sharedPref = getSharedPreferences("AppData", Context.MODE_PRIVATE)
        sharedPref?.edit()?.putInt("selectedTemplateIndex", selectedTemplateIndex)?.apply()

        setContentView(R.layout.activity_generate_meme)
    }
}