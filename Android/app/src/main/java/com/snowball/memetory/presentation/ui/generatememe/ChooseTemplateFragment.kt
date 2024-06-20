package com.snowball.memetory.presentation.ui.generatememe

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.snowball.memetory.R
import com.snowball.memetory.data.api.NetworkModule
import com.snowball.memetory.data.repository.GenerateMemeRepository
import com.snowball.memetory.databinding.FragmentChooseTemplateBinding
import com.snowball.memetory.presentation.ui.MainActivity
import com.snowball.memetory.presentation.ui.generatememe.adapter.TemplateDetailVPAdater
import com.snowball.memetory.presentation.ui.generatememe.scenedetail.SceneDetailViewModel
import com.snowball.memetory.presentation.ui.generatememe.scenedetail.SceneDetailViewModelFactory
import com.snowball.memetory.presentation.ui.generatememe.voice.ChooseVoiceFragmentDirections
import kotlin.math.abs

class ChooseTemplateFragment : Fragment() {

    lateinit var navController: NavController
    lateinit var binding: FragmentChooseTemplateBinding
    lateinit var templateVPAdapter: TemplateDetailVPAdater

//    private val viewModel: SceneDetailViewModel by activityViewModels()  // ViewModel 인스턴스 생성
    private lateinit var viewModel: SceneDetailViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_choose_template, container, false)

        val generateMemeService = NetworkModule.generateMemeService
        val generateMemeRepository = GenerateMemeRepository(generateMemeService)
        val viewModelFactory = SceneDetailViewModelFactory(generateMemeRepository)
        viewModel = ViewModelProvider(requireActivity(), viewModelFactory).get(SceneDetailViewModel::class.java)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = Navigation.findNavController(view)

        binding.confirmBtn.setOnClickListener {
            viewModel.generateMeme()
            Toast.makeText(context, "밈이 만들어지고 있습니다.", Toast.LENGTH_LONG).show()
            // 이전에 사용한 액티비티 종료 및 MainActivity(HomeFragment)로 이동
            val intent = Intent(context, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }
        viewModel.selectedTemplateIndex.observe(viewLifecycleOwner) { index ->
            updateViewPagerImages(index)
        }
    }
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

//        var imgRes = arrayListOf<Int>(
//            R.drawable.demon1,
//            R.drawable.demon2,
//            R.drawable.demon3,
//            R.drawable.world1,
//            R.drawable.world2,
//            R.drawable.world3,
//            R.drawable.horseking1,
//            R.drawable.horseking2,
//            R.drawable.horseking3,
//
//            )
//
//        var templtVPAdapter = TemplateDetailVPAdater(imgRes)
//        binding.templateViewPager.adapter = templtVPAdapter

        val sharedPref = activity?.getSharedPreferences("AppData", Context.MODE_PRIVATE)
        val selectedTemplateIndex = sharedPref?.getInt("selectedTemplateIndex", 0) // 기본값 0

        updateViewPagerImages(selectedTemplateIndex ?: 0)
        Log.e("ChooseTemplateFragment", "$selectedTemplateIndex")

        setOffsetBetweenPages()

//        templateVPAdapter.itemClickListener = object : TemplateDetailVPAdater.OnItemClickListener {
//            override fun onItemClick(view: View, position: Int) {
//                var index = (position+1).toString()
//                val action = ChooseTemplateFragmentDirections.actionChooseTemplateFragmentToSceneDetailFragment(index, "")
//                Log.d("SceneDetailFragment", "ChooseTemplateFragment: ${position+1}")
//                findNavController().navigate(action)
//
//            }
//        }

        binding.templateViewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() { // 페이지 바뀔때마다
            override fun onPageSelected(position: Int) { // 이 페이지의 번호가 선택되면
                super.onPageSelected(position)

//                // 현재 페이지 번호를 ViewModel에 저장
//                viewModel.selectedTemplateId = position
                Log.d("ViewPagerFragment", "Page ${position+1}")
            }
        })

    }

    private fun updateViewPagerImages(index: Int) {
        val imageResources = when (index) {
            0 -> listOf(R.drawable.demon1, R.drawable.demon2, R.drawable.demon3)
            1 -> listOf(R.drawable.world1, R.drawable.world2, R.drawable.world3)
            2 -> listOf(R.drawable.horseking1, R.drawable.horseking2, R.drawable.horseking3)
            else -> emptyList()
        }

        templateVPAdapter = TemplateDetailVPAdater(imageResources).apply {
            itemClickListener = object : TemplateDetailVPAdater.OnItemClickListener {
                override fun onItemClick(view: View, position: Int) {
                    val globalPosition = index * 3 + position + 1
//                    viewModel.setTargetImage(globalPosition.toString())
                    val action = ChooseTemplateFragmentDirections.actionChooseTemplateFragmentToSceneDetailFragment(globalPosition.toString(), "")
                    findNavController().navigate(action)
                }
            }
        }
        binding.templateViewPager.adapter = templateVPAdapter
    }

    private fun setOffsetBetweenPages() {
        // 관리하는 페이지 수. default = 1
        binding.templateViewPager.offscreenPageLimit = 4
        // item_view 간의 양 옆 여백을 상쇄할 값
        val offsetBetweenPages =
            resources.getDimensionPixelOffset(R.dimen.offsetBetweenPages).toFloat()
        binding.templateViewPager.setPageTransformer { page, position ->
            val myOffset = position * -(2 * offsetBetweenPages)
            if (position < -1) {
                page.translationX = -myOffset
            } else if (position <= 1) {
                // Paging 시 Y축 Animation 배경색을 약간 연하게 처리
                val scaleFactor = 0.8f.coerceAtLeast(1 - abs(position))
                page.translationX = myOffset
                page.scaleY = scaleFactor
                page.alpha = scaleFactor
            } else {
                page.alpha = 0f
                page.translationX = myOffset
            }
        }
    }

}