package com.snowball.memetory.presentation.ui.tutorial

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.snowball.memetory.databinding.FragmentTutorialThirdBinding
import com.snowball.memetory.presentation.ui.MainActivity

class TutorialThirdFragment : Fragment() {

    private var _binding: FragmentTutorialThirdBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentTutorialThirdBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.confirmBtn.setOnClickListener {
            startActivity(Intent(activity, MainActivity::class.java))
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}