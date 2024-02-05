package com.snowball.memetory.presentation.ui.auth

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.snowball.memetory.R
import com.snowball.memetory.databinding.FragmentSignInBinding
import com.snowball.memetory.databinding.FragmentSignUpBinding

class SignUpFragment : Fragment() {
    private var _binding: FragmentSignUpBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentSignUpBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.confirmBtn.setOnClickListener {
            // AuthActivity의 메소드를 호출하여 CircleIndicator와 ViewPager를 표시합니다.
            (activity as? AuthActivity)?.showTutorialViews()
            findNavController().navigate(R.id.tutorialFirstFragment) // Navigate to another fragment
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null

    }
}