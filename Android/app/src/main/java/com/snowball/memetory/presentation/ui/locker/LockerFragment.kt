package com.snowball.memetory.presentation.ui.locker

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController

import com.snowball.memetory.R
import com.snowball.memetory.data.api.NetworkModule
import com.snowball.memetory.data.repository.LockerRepository
import com.snowball.memetory.databinding.FragmentLockerBinding
import com.snowball.memetory.presentation.ui.generatememe.GenerateMemeActivity
import com.snowball.memetory.presentation.ui.locker.adapter.VideoRVAdapter


class LockerFragment : Fragment() {

    lateinit var binding: FragmentLockerBinding
    private lateinit var viewModel: LockerViewModel
    private lateinit var videoRVAdapter: VideoRVAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val lockerService = NetworkModule.lockerService
        val lockerRepository = LockerRepository(lockerService)
        val viewModelFactory = LockerViewModelFactory(lockerRepository)
        viewModel = ViewModelProvider(this, viewModelFactory).get(LockerViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_locker, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getAllMeme(0, 10)
        // DiffUtil
        viewModel.videoUrls.observe(viewLifecycleOwner) { videos ->
            videoRVAdapter.submitList(videos)
        }
        setupRecyclerView()

//        viewModel.videoUrls.observe(viewLifecycleOwner) { videos ->
//            videoRVAdapter.updateData(videos)  // 어댑터에 데이터를 설정합니다.
//        }


    }

    private fun setupRecyclerView() {
        videoRVAdapter = VideoRVAdapter { videoUrl ->
            navigateToPlayerFragment(videoUrl)
        }
        binding.recyclerView.adapter = videoRVAdapter
    }

    private fun navigateToPlayerFragment(videoUrl: String) {
        val intent = Intent(activity, LockerPlayerActivity::class.java)
        Log.d("LockerFragment", "$videoUrl")
        intent.putExtra("videoUrl", videoUrl)
        startActivity(intent)
    }
}