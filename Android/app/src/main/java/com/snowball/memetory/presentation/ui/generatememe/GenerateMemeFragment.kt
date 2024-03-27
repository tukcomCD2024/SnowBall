package com.snowball.memetory.presentation.ui.generatememe

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import com.snowball.memetory.R
import com.snowball.memetory.databinding.FragmentGenerateMemeBinding
import com.snowball.memetory.presentation.ui.generatememe.adapter.TemplateRVAdater

class GenerateMemeFragment : Fragment(), TemplateRVAdater.OnItemClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentGenerateMemeBinding = DataBindingUtil.inflate(inflater,
                                                    R.layout.fragment_generate_meme,
                                                    container,
                                                    false)
        val arrayList = arrayListOf<Int>(3)
        binding.templateRecyclerView.adapter = TemplateRVAdater(arrayList, this)

        return binding.root
        
    }

    override fun onItemClick(view: View, position: Int) {
        val intent = Intent(activity, GenerateMemeActivity::class.java)
        // 필요한 경우 intent에 데이터 추가
        // intent.putExtra("extra_data", value)
        startActivity(intent)
    }

}