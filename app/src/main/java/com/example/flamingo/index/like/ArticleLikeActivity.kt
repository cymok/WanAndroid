package com.example.flamingo.index.like

import android.os.Bundle
import com.example.flamingo.R
import com.example.flamingo.base.activity.VBaseActivity
import com.example.flamingo.databinding.ActivityArticleListBinding

class ArticleLikeActivity : VBaseActivity<ActivityArticleListBinding>() {

    override val binding by lazy { ActivityArticleListBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) {

            supportActionBar?.title = "收藏博文"
            supportFragmentManager.beginTransaction()
                .replace(
                    binding.root.id,
                    ArticleLikeFragment()
                )
                .commitNow()

        }
    }

    override fun initStatusBarDarkFont() = true

    override fun initStatusBarColor() = R.color.status_bar

}