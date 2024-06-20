package com.snowball.memetory.presentation.ui.generatememe

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.snowball.memetory.R
import com.snowball.memetory.data.api.NetworkModule
import com.snowball.memetory.data.repository.GenerateMemeRepository
import com.snowball.memetory.databinding.FragmentGenerateMemeBinding
import com.snowball.memetory.presentation.ui.generatememe.adapter.TemplateRVAdater
import com.snowball.memetory.presentation.ui.generatememe.scenedetail.SceneDetailViewModel
import com.snowball.memetory.presentation.ui.generatememe.scenedetail.SceneDetailViewModelFactory

class GenerateMemeFragment : Fragment(), TemplateRVAdater.OnItemClickListener {

    private lateinit var viewModel: SceneDetailViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentGenerateMemeBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_generate_meme, container, false
        )
        val generateMemeService = NetworkModule.generateMemeService
        val generateMemeRepository = GenerateMemeRepository(generateMemeService)
        val viewModelFactory = SceneDetailViewModelFactory(generateMemeRepository)
        viewModel = ViewModelProvider(requireActivity(), viewModelFactory).get(SceneDetailViewModel::class.java)

        // Assuming you have 3 items and you want each row to have 2 columns
        val gridLayoutManager = GridLayoutManager(context, 2)
        binding.templateRecyclerView.layoutManager = gridLayoutManager

        // Example list of images (You might want to replace these with actual drawable resource IDs)
        val imageList = arrayListOf(R.drawable.demon1, R.drawable.world1, R.drawable.horseking1)
        binding.templateRecyclerView.adapter = TemplateRVAdater(imageList, this)

        return binding.root
    }

    override fun onItemClick(view: View, position: Int) {
        viewModel.setSelectedTemplateIndex(position)
        Log.e("GenerateMemeFragment", "템플릿 클릭된 position값(selectedTemplateIndex): $position")
        val intent = Intent(activity, GenerateMemeActivity::class.java)
        intent.putExtra("selectedTemplateIndex", viewModel.selectedTemplateIndex.value)

        // Intent data can be added here if needed
        startActivity(intent)
    }

}