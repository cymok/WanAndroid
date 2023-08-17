package com.example.wan.android.index.home

import android.os.Bundle
import com.example.wan.android.R
import com.example.wan.android.base.activity.VBaseActivity
import com.example.wan.android.databinding.ActivityArticleListBinding

class HomeActivity : VBaseActivity<ActivityArticleListBinding>() {

    override val binding by lazy { ActivityArticleListBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) {

            supportActionBar?.title = "推荐"
            supportFragmentManager.beginTransaction()
                .replace(
                    binding.root.id,
                    HomeFragment.getInstance(false)
                )
                .commitNow()

        }
    }

    override fun initStatusBarColor() = R.color.status_bar

}