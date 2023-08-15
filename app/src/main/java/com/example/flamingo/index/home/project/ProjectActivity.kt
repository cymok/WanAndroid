package com.example.flamingo.index.home.project

import android.os.Bundle
import com.example.flamingo.R
import com.example.flamingo.base.activity.VBaseActivity
import com.example.flamingo.data.ArticlePage
import com.example.flamingo.databinding.ActivityArticleListBinding
import com.example.flamingo.ui.getCurrent

class ProjectActivity : VBaseActivity<ActivityArticleListBinding>() {

    override val binding by lazy { ActivityArticleListBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) {
            supportActionBar?.title = "项目"
            supportFragmentManager.beginTransaction()
                .replace(
                    binding.root.id,
                    ProjectFragment.getInstance(false),
                )
                .commitNow()
        }
    }

    override fun initStatusBarColor() = R.color.status_bar

}