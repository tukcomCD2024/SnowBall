package com.snowball.memetory.presentation.ui.generatememe

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.snowball.memetory.R
import com.snowball.memetory.databinding.FragmentChooseFaceBinding

class ChooseFaceFragment : Fragment() {

    lateinit var navController: NavController
    lateinit var binding: FragmentChooseFaceBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_choose_face, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
        binding.confirmBtn.setOnClickListener {
            navController.navigate(R.id.action_chooseFaceFragment_to_sceneDetailFragment)
        }
    }
}