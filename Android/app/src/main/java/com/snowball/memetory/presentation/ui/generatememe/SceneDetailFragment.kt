package com.snowball.memetory.presentation.ui.generatememe

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Resources
import android.database.Cursor
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomViewTarget
import com.snowball.memetory.R
import com.snowball.memetory.data.dto.generatememe.SceneDetailDto
import com.snowball.memetory.databinding.FragmentSceneDetailBinding
import com.snowball.memetory.util.S3Util
import java.io.File

class SceneDetailFragment : Fragment() {

    lateinit var navController: NavController
    lateinit var binding: FragmentSceneDetailBinding
    private val TAG1 = "ChooseFace"
    private val TAG2 = "EnterLines"
    private val viewModel: SceneDetailViewModel by activityViewModels()

    private val Int.dp: Int get() = (this * Resources.getSystem().displayMetrics.density).toInt() // px 단위를 dp로 변경
    private var imageFile = File("")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_scene_detail, container, false)

        initTextChangedListener()
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
        initOnClickListener()

//        viewModel.selectedImageUri?.let {
//            loadImage(it)
//        }
//
//        // ViewModel에 저장된 텍스트를 EditText에 설정
//        viewModel.enteredText?.let {
//            binding.linesEditText.setText(it)
//        }
        Log.d("SceneDetail", "${viewModel.selectedTemplateId}")
// 데이터클래스 두개로 묶으면 전부다 널이 아니어야만 저렇게 뷰로 나오는게 가능함. 그니까 이미지뷰와 대사를 둘다
// 했어야만 지금과같이 뷰가 나오면 그대로 유지되는것을 볼 수 있는거임. 따라서 별개로 했을때? 예외처리 필요
        viewModel.selectedTemplateId?.let { templateId ->
            viewModel.getTemplateData(templateId)?.let { templateData ->
                binding.linesEditText.setText(templateData.text)
                loadImage(templateData.imageUri)
            }
        }

    }

    private fun initOnClickListener() {
        binding.chooseFaceImg.setOnClickListener {
            requestGalleryAccess()
        }

        binding.chooseVoiceBtn.setOnClickListener {
            // 현재 선택된 템플릿 ID 가져오기
            val currentTemplateId = viewModel.selectedTemplateId

            currentTemplateId?.let { templateId ->
                // 임시 저장된 이미지 URI와 텍스트를 가져와서 SceneDetailDto 객체 생성
                val templateData = SceneDetailDto(viewModel.selectedImageUri, viewModel.enteredText)
                // 템플릿 데이터 맵에 현재 템플릿 데이터 저장
                viewModel.setTemplateData(templateId, templateData)
            }
            navController.navigate(R.id.action_sceneDetailFragment_to_chooseVoiceFragment)
        }

        binding.confirmBtn.setOnClickListener {
            // 현재 선택된 템플릿 ID 가져오기
            val currentTemplateId = viewModel.selectedTemplateId

            currentTemplateId?.let { templateId ->
                // 임시 저장된 이미지 URI와 텍스트를 가져와서 SceneDetailDto 객체 생성
                val templateData = SceneDetailDto(viewModel.selectedImageUri, viewModel.enteredText)
                // 템플릿 데이터 맵에 현재 템플릿 데이터 저장
                viewModel.setTemplateData(templateId, templateData)
            }
            navController.navigate(R.id.action_sceneDetailFragment_to_chooseTemplateFragment)
        }
    }

    // 얼굴 선택
    // 갤러리 접근 권한 요청 변수
    private val galleryPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted){
                openGallery()
            } else {
                Log.d(TAG1, "접근 권한 요청 거절")
                Toast.makeText(requireContext(), "허용하셔야 이미지를 불러오실 수 있습니다.", Toast.LENGTH_LONG).show()
            }

        }

    // 이미지를 결과값으로 받는 변수
    private val imageLauncher =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ){ result ->
            Log.d(TAG1, "imageLauncher enter!!")
            if (result.resultCode == Activity.RESULT_OK) { // 이미지를 받으면 ImageView에 적용
                val imageUri = result.data?.data
                val s3Util = S3Util(requireContext())

                imageUri?.let {
                    imageFile = File(getRealPathFromURI(it)) // 서버 업로드를 위해 파일 형태로 변환
                    val fileToUpload = imageFile
                    s3Util.uploadFile("image2.jpeg", fileToUpload)
                    Log.d(TAG1, imageFile.toString())

//                    loadImage(imageUri) // ImageView에 적용

                    viewModel.selectedImageUri = it // ViewModel에 Uri 저장
                    loadImage(it) // 이미지 불러오기

                }
            }
        }

    // 이미지 실제 경로 반환
    fun getRealPathFromURI(contentUri: Uri): String? {
        var cursor: Cursor? = null
        try {
            val proj = arrayOf(MediaStore.Images.Media.DATA)
            cursor = activity?.contentResolver?.query(contentUri, proj, null, null, null)
            val columnIndex = cursor?.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
            cursor?.moveToFirst()
            return columnIndex?.let { cursor!!.getString(it) }
        } finally {
            cursor?.close()
        }
    }
    // 이미지 불러오기 함수
    private fun loadImage(imageUri: Uri?) {
//                    // 이미지뷰의 크기를 변경하고 싶다면 여기서 layout params를 조정
        binding.chooseFaceImg.layoutParams.width = 300.dp
        binding.chooseFaceImg.layoutParams.height = 300.dp
        Glide.with(this)
            .load(imageUri)
            .fitCenter()
            .into(binding.chooseFaceImg)
    }
//    // 이미지 불러오기 (ImageView에 적용)
//    private fun loadImage(imageUri: Uri?) =
//        Glide.with(this)
//            .load(imageUri)
//            .fitCenter()
//            .into(object : CustomViewTarget<ImageView, Drawable>(binding.chooseFaceImg) {
//                // 이미지가 준비되었을 때의 처리
//                override fun onResourceReady(
//                    resource: Drawable,
//                    transition: com.bumptech.glide.request.transition.Transition<in Drawable>?
//                ) {
//                    binding.chooseFaceImg.setImageDrawable(resource)
//                    // 이미지뷰의 크기를 변경하고 싶다면 여기서 layout params를 조정
//                    binding.chooseFaceImg.layoutParams.width = 300.dp
//                    binding.chooseFaceImg.layoutParams.height = 300.dp
//                }
//
//                override fun onLoadFailed(errorDrawable: Drawable?) {
//                    // 로드 실패 시의 처리
//                }
//
//                override fun onResourceCleared(placeholder: Drawable?) {
//                    // 이미지가 선택되고 난 후 다시 그 이미지를 선택했을 때 처리
//                    binding.chooseFaceImg.setImageDrawable(null)
//                }
//            })

    // 안드로이드 버전에 따른 접근 권한 요청 및 대화 상자 열기
    private fun requestGalleryAccess() {
        val permissionToCheck = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            android.Manifest.permission.READ_MEDIA_IMAGES
        } else {
            android.Manifest.permission.READ_EXTERNAL_STORAGE
        }

        when {
            ContextCompat.checkSelfPermission(requireContext(), permissionToCheck) == PackageManager.PERMISSION_GRANTED -> {
                // 권한이 이미 승인되어 있으면 갤러리를 열어서 이미지를 선택
                openGallery()
            }
            shouldShowRequestPermissionRationale(permissionToCheck) -> {
                // 사용자가 권한 요청을 거절했고, '다시 묻지 않음'을 선택하지 않았을 경우
                // 사용자에게 왜 권한이 필요한지 설명하고 다시 권한을 요청할 수 있도록 한다.
                galleryPermissionLauncher.launch(permissionToCheck)
            }
            else -> {
                // 사용자가 권한 요청을 거절했거나 '다시 묻지 않음'을 선택했을 경우,
                // 권한 요청을 위한 시스템 대화상자를 표시한다.
                Log.d(TAG1, "다시 묻지 않음")
                Toast.makeText(requireContext(), "휴대폰에서 갤러리 접근 권한을 허용해주세요.", Toast.LENGTH_LONG).show()
            }
        }
    }

    // 휴대폰 갤러리 열기
    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*")
        imageLauncher.launch(intent)
    }

    // 대사 입력
    private fun initTextChangedListener() {
        binding.linesEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
//                이 메소드는 텍스트가 변경되기 전에 호출
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
//                이 메소드는 텍스트가 변경되고 있는 동안 호출
            }
            override fun afterTextChanged(s: Editable?) {
//                이 메소드는 텍스트가 변경된 후에 호출
                viewModel.enteredText = s.toString() // 사용자가 입력한 텍스트를 ViewModel에 저장
            }
        })
    }
// 목소리 선택
}