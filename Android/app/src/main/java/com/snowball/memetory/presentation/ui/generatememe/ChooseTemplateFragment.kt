package com.snowball.memetory.presentation.ui.generatememe

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.viewpager2.widget.ViewPager2
import com.snowball.memetory.R
import com.snowball.memetory.data.dto.generatememe.SceneDetailDto
import com.snowball.memetory.databinding.FragmentChooseTemplateBinding
import com.snowball.memetory.presentation.ui.generatememe.adapter.TemplateDetailVPAdater
import com.snowball.memetory.presentation.ui.generatememe.adapter.TemplateRVAdater
import kotlin.math.abs

class ChooseTemplateFragment : Fragment() {

    lateinit var navController: NavController
    lateinit var binding: FragmentChooseTemplateBinding
    private val viewModel: SceneDetailViewModel by activityViewModels()  // ViewModel 인스턴스 생성

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_choose_template, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = Navigation.findNavController(view)

        binding.confirmBtn.setOnClickListener {
            navController.navigate(R.id.action_chooseTemplateFragment_to_previewFragment)
        }
    }
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

//        var imgRes = arrayListOf<Int>(
//            R.drawable.gamst,
//            R.drawable.ic_play_circle_32,
//            R.drawable.gamst,
//            R.drawable.ic_play_circle_32
//        )

        var imgRes = arrayListOf<Int>(
            R.drawable.snake,
            R.drawable.malfoy
        )

        var templtVPAdapter = TemplateDetailVPAdater(imgRes)
        binding.templateViewPager.adapter = templtVPAdapter
        setOffsetBetweenPages()

        templtVPAdapter.itemClickListener = object : TemplateDetailVPAdater.OnItemClickListener {
            override fun onItemClick(view: View, position: Int) {
                val resourceId = imgRes[position]
                Log.d("ChooseTemplate", "$position+$resourceId")
                val isSelected = viewModel.isImageSelected(resourceId)
                viewModel.selectedTemplateId = position
                viewModel.setImageSelected(resourceId, !isSelected)
                Log.d("ChooseTemplate", "${viewModel.selectedTemplateId}")

                // 현재 선택된 템플릿의 정보를 ViewModel에 저장
//                val currentData = SceneDetailDto(viewModel.selectedImageUri, viewModel.enteredText)
//                viewModel.setTemplateData(resourceId, currentData)

                navController.navigate(R.id.action_chooseTemplateFragment_to_sceneDetailFragment)

            }
        }

        binding.templateViewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() { // 페이지 바뀔때마다
            override fun onPageSelected(position: Int) { // 이 페이지의 번호가 선택되면
                super.onPageSelected(position)
//                // 현재 페이지 번호를 ViewModel에 저장
//                viewModel.selectedTemplateId = position
                Log.d("ViewPagerFragment", "Page ${position+1}")
            }
        })

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