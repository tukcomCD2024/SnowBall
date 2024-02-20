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
import com.snowball.memetory.databinding.FragmentChooseVoiceBinding
import com.snowball.memetory.presentation.ui.generatememe.adapter.VoiceRVAdapter

class ChooseVoiceFragment : Fragment() {

    lateinit var navController: NavController
    lateinit var binding: FragmentChooseVoiceBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_choose_voice, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val arryList = arrayListOf<String>(
            "hello",
            "맨유",
            "손",
            "겜스트",
            "맹까",
            "맹박사",
            "ㅂㅂ"
        )
        binding.voiceRecyclerView.adapter = VoiceRVAdapter(arryList)

        navController = Navigation.findNavController(view)
        binding.confirmBtn.setOnClickListener {
            navController.navigate(R.id.action_chooseVoiceFragment_to_sceneDetailFragment)
        }
    }

}