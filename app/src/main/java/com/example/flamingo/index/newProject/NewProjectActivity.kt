package com.example.flamingo.index.newProject

import android.os.Bundle
import com.example.flamingo.R
import com.example.flamingo.base.activity.VBaseActivity
import com.example.flamingo.databinding.ActivityArticleListBinding

/**
 * 最新项目 已经整合进项目分类 关掉单独页面的入口
 */
class NewProjectActivity : VBaseActivity<ActivityArticleListBinding>() {

    override val binding by lazy { ActivityArticleListBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) {

            supportActionBar?.title = "最新项目"
            supportFragmentManager.beginTransaction()
                .replace(
                    binding.root.id,
                    NewProjectFragment()
                )
                .commitNow()

        }
    }

    override fun initStatusBarColor() = R.color.status_bar

}