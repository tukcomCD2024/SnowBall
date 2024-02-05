package com.snowball.memetory.presentation.ui.auth

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.snowball.memetory.presentation.ui.tutorial.TutorialFirstFragment
import com.snowball.memetory.presentation.ui.tutorial.TutorialSecondFragment
import com.snowball.memetory.presentation.ui.tutorial.TutorialThirdFragment

class TutorialVPAdapter(fragmentActivity: FragmentActivity): FragmentStateAdapter(fragmentActivity) {

//    var fragments: ArrayList<Fragment> = ArrayList()

    // 페이지 갯수 설정
    override fun getItemCount(): Int = 3

    // 불러올 Fragment 정의
    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> TutorialFirstFragment()
            1 -> TutorialSecondFragment()
            2 -> TutorialThirdFragment()
            else -> throw IllegalArgumentException("Invalid position $position")
        }
    }
}