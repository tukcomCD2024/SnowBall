package com.snowball.memetory.presentation.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.snowball.memetory.R
import com.snowball.memetory.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    lateinit var navController: NavController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val bottomNavigationView = binding.bottomNavigation
        navController = findNavController(R.id.mainNavHost)

        setBottomNavigationSelection() // 메뉴와 바텀 네비게이션 뷰의 아이템 설정
        setBottomNavigationView(bottomNavigationView) // 바텀 네비게이션 뷰의 아이템 선택 리스너 설정

    }

    private fun setBottomNavigationSelection() {
        val currentFragmentId = navController.currentDestination?.id
        val menu = binding.bottomNavigation.menu
        val menuItemId = when (currentFragmentId) {
            R.id.lockerFragment -> R.id.lockerFragment // menu_locker는 bottomNavigationView의 아이템 ID입니다.
            R.id.homeFragment -> R.id.homeFragment
            R.id.generateMemeFragment -> R.id.generateMemeFragment
            R.id.memesFragment -> R.id.memesFragment
            else -> R.id.homeFragment // 기본값으로 홈을 설정
        }
        menu.findItem(menuItemId)?.isChecked = true
    }

    private fun setBottomNavigationView(bottomNavigationView: BottomNavigationView) {
        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.homeFragment -> {
                    navController.navigate(R.id.homeFragment)
                    true
                }

                R.id.generateMemeFragment -> {
                    navController.navigate(R.id.generateMemeFragment)
                    true
                }

                R.id.memesFragment -> {
                    navController.navigate(R.id.memesFragment)
                    true
                }

                R.id.lockerFragment -> {
                    navController.navigate(R.id.lockerFragment)
                    true
                }
                else -> false
            }
        }
    }

}