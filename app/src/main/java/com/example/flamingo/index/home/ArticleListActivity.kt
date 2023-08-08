package com.example.flamingo.index.home

import android.content.Intent
import android.os.Bundle
import com.blankj.utilcode.util.ActivityUtils
import com.example.flamingo.App
import com.example.flamingo.R
import com.example.flamingo.base.activity.VBaseActivity
import com.example.flamingo.data.ArticlePage
import com.example.flamingo.databinding.ActivityArticlesBinding
import com.example.flamingo.index.home.home.HomeViewModel
import com.example.flamingo.utils.getViewModel

class ArticleListActivity : VBaseActivity<ActivityArticlesBinding>() {

    companion object {

        fun start(@ArticlePage whichPage: Int) {
            ActivityUtils.startActivity(
                Intent(App.INSTANCE, ArticleListActivity::class.java).apply {
                    putExtra("whichPage", whichPage)
                }
            )
        }
    }

    override val binding by lazy { ActivityArticlesBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(binding.root.id, ArticleListFragment().apply {
                    arguments = Bundle().apply {
                        val whichPage = intent.getIntExtra("whichPage", -1)
                        putInt("whichPage", whichPage)
//                        putParcelable("data", it)
                    }
                }
                )
                .commitNow()
        }
    }

    override fun initStatusBarDarkFont() = true

    override fun initNavBarColor() = R.color.transparent

}