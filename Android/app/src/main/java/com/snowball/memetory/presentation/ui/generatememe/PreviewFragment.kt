package com.snowball.memetory.presentation.ui.generatememe

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.snowball.memetory.R
import com.snowball.memetory.databinding.FragmentPreviewBinding

class PreviewFragment : Fragment() {

    lateinit var navController: NavController
    lateinit var binding: FragmentPreviewBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_preview, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = Navigation.findNavController(view)
        // 추후에 setOnClickListener를 confirmBtn이 아니라 리사이클러뷰에 있는 아이템으로 변경해야함.
        // 아직 아이템 만들지 않고 연결을 안했음.
        binding.confirmBtn.setOnClickListener {
            navController.navigate(R.id.action_previewFragment_to_previewDetailFragment)
        }
    }

}