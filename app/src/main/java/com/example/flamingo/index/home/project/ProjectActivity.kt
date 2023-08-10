package com.example.flamingo.index.home.project

import android.os.Bundle
import com.example.flamingo.R
import com.example.flamingo.base.activity.VBaseActivity
import com.example.flamingo.data.ArticlePage
import com.example.flamingo.databinding.ActivityArticleListBinding
import com.example.flamingo.ui.getCurrent

class ProjectActivity : VBaseActivity<ActivityArticleListBinding>() {

    override val binding by lazy { ActivityArticleListBinding.inflate(layoutInflater) }

    val pagePath = arrayListOf(ArticlePage.PROJECT)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) {
            supportActionBar?.title = pagePath.getCurrent()
            supportFragmentManager.beginTransaction()
                .replace(
                    binding.root.id,
                    ProjectFragment.getInstance(false),
                )
                .commitNow()
        }
    }

    override fun initStatusBarDarkFont() = true

    override fun initStatusBarColor() = R.color.status_bar

}