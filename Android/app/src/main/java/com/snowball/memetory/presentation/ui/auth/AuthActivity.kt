package com.snowball.memetory.presentation.ui.auth

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.view.isVisible
import com.snowball.memetory.databinding.ActivityAuthBinding

class AuthActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAuthBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAuthBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val adapter = TutorialVPAdapter(this)
        val viewPager = binding.tutorialViewPager
        val circleIndicator = binding.circleIndicator
        viewPager.adapter = adapter
        circleIndicator.setViewPager(viewPager)
//        adapter.registerAdapterDataObserver(circleIndicator.getAdapterDataObserver());

    }

    fun showTutorialViews() {
        binding.naviHostFragment.isVisible = false
        binding.circleIndicator.isVisible = true
        binding.tutorialViewPager.isVisible = true
    }
}