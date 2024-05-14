package com.snowball.memetory.presentation.ui.generatememe.voice

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.snowball.memetory.R
import com.snowball.memetory.data.api.GenerateMemeService
import com.snowball.memetory.data.api.NetworkModule
import com.snowball.memetory.data.repository.VoiceRepository
import com.snowball.memetory.databinding.FragmentChooseVoiceBinding
import com.snowball.memetory.presentation.ui.generatememe.adapter.VoiceRVAdapter
import com.snowball.memetory.util.FileUtil
import com.snowball.memetory.util.S3Util


class ChooseVoiceFragment : Fragment(), VoiceRVAdapter.OnItemClickListener  {


    lateinit var navController: NavController
    lateinit var binding: FragmentChooseVoiceBinding
    lateinit var rvAdapter: VoiceRVAdapter

    private lateinit var viewModel: VoiceViewModel
    private lateinit var permissionLauncher: ActivityResultLauncher<String>
    private lateinit var audioPickerLauncher: ActivityResultLauncher<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Repository 초기화 (예시)
        val generateMemeService = NetworkModule.generateMemeService
        val voiceRepository = VoiceRepository(generateMemeService) // 이 부분은 구현에 따라 달라집니다.
        val viewModelFactory = VoiceViewModelFactory(voiceRepository)
        viewModel = ViewModelProvider(this, viewModelFactory).get(VoiceViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_choose_voice, container, false)
        setupLaunchers()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val arryList = arrayListOf<String>(
            "hello",
            "맨유",
            "손",
            "겜스트",
        )
        binding.chooseVoiceBtn.setOnClickListener {
                // Check if permission is granted
            if (FileUtil.hasStoragePermission(requireContext())) {
                FileUtil.openAudioPicker(audioPickerLauncher)
            } else {
                permissionLauncher.launch(android.Manifest.permission.READ_EXTERNAL_STORAGE)
            }
        }

        rvAdapter = VoiceRVAdapter(arryList, this)
        binding.voiceRecyclerView.adapter = rvAdapter

        navController = Navigation.findNavController(view)
        binding.confirmBtn.setOnClickListener {
            navController.navigate(R.id.action_chooseVoiceFragment_to_sceneDetailFragment)
        }
    }

    override fun onItemClick(view: View, position: Int) {
        Toast.makeText(requireContext(), "$position", Toast.LENGTH_LONG).show()
    }

    private fun setupLaunchers() {
        // Initialize the permission launcher
        permissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                FileUtil.openAudioPicker(audioPickerLauncher)
            } else {
                Toast.makeText(requireContext(), "접근 권한이 필요합니다.", Toast.LENGTH_LONG).show()
            }
        }

        audioPickerLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            uri?.let {
                viewModel.uploadFileAndHandle(requireContext(), it)
            }
        }
    }

}