package com.snowball.memetory.presentation.ui.generatememe.voice

import android.os.Bundle
import android.util.Log
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
import com.snowball.memetory.domain.model.voice.Voice
import com.snowball.memetory.presentation.ui.generatememe.adapter.VoiceRVAdapter
import com.snowball.memetory.util.FileUtil
import com.snowball.memetory.util.S3Util


class ChooseVoiceFragment : Fragment(), VoiceRVAdapter.OnItemClickListener  {


    lateinit var navController: NavController
    lateinit var binding: FragmentChooseVoiceBinding

    private lateinit var viewModel: VoiceViewModel
    private lateinit var permissionLauncher: ActivityResultLauncher<String>
    private lateinit var audioPickerLauncher: ActivityResultLauncher<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Repository 초기화
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

        viewModel.voices.observe(viewLifecycleOwner) { voices ->
            setupRecyclerView(voices)  // 리사이클러뷰를 설정하는 함수
        }
        viewModel.getVoiceIdList()  // 서버에서 목소리 목록 가져오기

        // 목소리 가져오기 버튼 클릭시 파일 권한 검사
        binding.chooseVoiceBtn.setOnClickListener {
            // Check if permission is granted
            if (FileUtil.hasStoragePermission(requireContext())) {
                FileUtil.openAudioPicker(audioPickerLauncher)
            } else {
                permissionLauncher.launch(android.Manifest.permission.READ_EXTERNAL_STORAGE)
            }
        }

        // 확인하기 버튼 클릭시 리사이클러뷰에서 선택한 라디오버튼에 대해서 voiceId값 가져옴.
        navController = Navigation.findNavController(view)
        binding.confirmBtn.setOnClickListener {
            val selectedVoiceId = viewModel.selectedVoiceId.value
            Log.d("ChooseVoiceFragment", "$selectedVoiceId")
            // 선택된 VoiceId로 필요한 작업 수행
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
    private fun setupRecyclerView(voices: List<Voice>) {
        val rvAdapter = VoiceRVAdapter(voices) { voice ->
            viewModel.selectVoice(voice)
        }
        binding.voiceRecyclerView.adapter = rvAdapter
    }

}