package com.example.flamingo.index.search

import android.os.Bundle
import com.example.flamingo.R
import com.example.flamingo.base.activity.VBaseActivity
import com.example.flamingo.databinding.ActivityArticleListBinding

class SearchActivity  : VBaseActivity<ActivityArticleListBinding>() {

    override val binding by lazy { ActivityArticleListBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) {

            supportActionBar?.title = "搜索"
            supportFragmentManager.beginTransaction()
                .replace(
                    binding.root.id,
                    SearchFragment()
                )
                .commitNow()

        }
    }

    override fun initStatusBarDarkFont() = true

    override fun initStatusBarColor() = R.color.status_bar

}