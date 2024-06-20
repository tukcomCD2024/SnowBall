package com.snowball.memetory.presentation.ui.locker

import android.app.AlertDialog
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.snowball.memetory.data.api.NetworkModule
import com.snowball.memetory.data.dto.locker.request.GenerateMemesRequestDto
import com.snowball.memetory.data.repository.LockerRepository
import com.snowball.memetory.databinding.FragmentVideoOptionsBottomSheetBinding
import kotlinx.coroutines.launch

class VideoOptionsBottomSheetFragment : BottomSheetDialogFragment() {

    private var _binding: FragmentVideoOptionsBottomSheetBinding? = null
    private val binding get() = _binding!!
    private lateinit var lockerRepository: LockerRepository
    private lateinit var viewModel: LockerViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val lockerService = NetworkModule.lockerService
        lockerRepository = LockerRepository(lockerService)
        val viewModelFactory = LockerViewModelFactory(lockerRepository)
        viewModel = ViewModelProvider(this, viewModelFactory).get(LockerViewModel::class.java)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentVideoOptionsBottomSheetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.uploadVideoBtn.setOnClickListener {
//            uploadVideo(arguments?.getString("videoUrl"))
//            val memeId = arguments?.getInt("memeId") ?: 0 // Handle null case or default
            uploadVideoToMemes(arguments?.getInt("memeId") ?: 0)
        }
    }

    private fun uploadVideoToMemes(memeId: Int) {
        Log.d("VideoOptionsBSFragment", "Requesting title for video: $memeId")

        val input = EditText(context).apply {
            inputType = InputType.TYPE_CLASS_TEXT
            hint = "Enter title"
        }

        val dialog = AlertDialog.Builder(context)
            .setTitle("밈스 영상 올리기")
            .setMessage("제목을 입력하고 입력 버튼을 클릭해주세요.")
            .setView(input)
            .setPositiveButton("입력") { dialog, whichButton ->
                val title = input.text.toString()
                uploadVideoWithTitle(memeId, title)
            }
            .setNegativeButton("취소") { dialog, whichButton ->
                dialog.dismiss()
            }
            .create()

        dialog.show()
    }

    private fun uploadVideoWithTitle(memeId: Int, title: String) {
        Log.d("VideoOptionsBSFragment", "Uploading video: $memeId with title: $title")
        // Here you can implement the logic to upload the video with the given title
        val request = GenerateMemesRequestDto(memeId, title)
        // Execute network call in coroutine scope tied to the fragment's lifecycle
        lifecycleScope.launch {
            val result = lockerRepository.generateMemes(request)
            result.onSuccess {
                Log.d("VideoOptionsBSFragment", "Video uploaded successfully: ${it.data}")
                // Handle success, update UI, etc.
            }.onFailure { exception ->
                Log.e("VideoOptionsBSFragment", "Failed to upload video: ${exception.message}")
                // Handle failure, show error message, etc.
            }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // Clear the binding reference to prevent memory leaks
    }

    companion object {
        fun newInstance(videoUrl: String, memeId: Int): VideoOptionsBottomSheetFragment {
            return VideoOptionsBottomSheetFragment().apply {
                arguments = Bundle().apply {
                    putString("videoUrl", videoUrl)
                    putInt("memeId", memeId)
                }
            }
        }
    }

//    companion object {
//        fun newInstance(videoUrl: String): VideoOptionsBottomSheetFragment {
//            return VideoOptionsBottomSheetFragment().apply {
//                arguments = Bundle().apply {
//                    putString("videoUrl", videoUrl)
//                }
//            }
//        }
//    }
}
