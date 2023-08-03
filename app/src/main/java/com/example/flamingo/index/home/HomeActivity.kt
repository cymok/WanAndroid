package com.example.flamingo.index.home

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.flamingo.R
import com.example.flamingo.base.BaseActivity
import com.example.flamingo.databinding.ActivityHomeBinding
import com.example.flamingo.utils.toastShort
import com.google.android.material.bottomnavigation.BottomNavigationItemView
import com.google.android.material.bottomnavigation.BottomNavigationMenuView
import com.google.android.material.bottomnavigation.BottomNavigationView

class HomeActivity : BaseActivity() {

    private val binding by lazy {
        ActivityHomeBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        navView.setupWithNavController(navController)
        navView.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.navigation_home -> {
                    toastShort("111")

                }

                R.id.navigation_dashboard -> {
                    toastShort("222")

                }

                R.id.navigation_square -> {
                    toastShort("333")

                }

                R.id.navigation_subscribe -> {
                    toastShort("444")

                }

                R.id.navigation_person -> {
                    toastShort("555")

                }
            }
            true
        }
    }

    @SuppressLint("RestrictedApi")
    private fun extracted(navView: BottomNavigationView) {
        try {
            val menuView = navView.getChildAt(0) as BottomNavigationMenuView
            for (i in 0 until menuView.childCount) {
                val itemView = menuView.getChildAt(i) as BottomNavigationItemView
                itemView.setShifting(false)
                itemView.setChecked(false)
            }
        } catch (ignore: Exception) {
        }
    }

}
