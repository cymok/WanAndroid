package com.example.flamingo.index.article

import android.content.Intent
import android.os.Bundle
import com.blankj.utilcode.util.ActivityUtils
import com.example.flamingo.App
import com.example.flamingo.R
import com.example.flamingo.base.activity.VBaseActivity
import com.example.flamingo.data.ArticlePage
import com.example.flamingo.databinding.ActivityArticlesBinding
import com.example.flamingo.utils.toast

class ArticleListActivity : VBaseActivity<ActivityArticlesBinding>() {

    companion object {

        fun start(@ArticlePage whichPage: String) {
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

            val whichPage = intent.getStringExtra("whichPage")

            supportActionBar?.title = whichPage

            when (whichPage) {
                ArticlePage.HOME,
                ArticlePage.SQUARE,
                -> {
                    supportFragmentManager.beginTransaction()
                        .replace(
                            binding.root.id,
                            ArticleListFragment.getInstance(whichPage, null)
                        )
                        .commitNow()
                }

/*
                ArticlePage.PROJECT,
                ArticlePage.SUBSCRIBE,
                -> {
                    supportFragmentManager.beginTransaction()
                        .replace(
                            binding.root.id,
                            ArticleTreeFragment.getInstance(whichPage, null)
                        )
                        .commitNow()
                }
*/

                else -> {
                    toast("开发中")
                    finish()
                }
            }

        }
    }

    override fun initStatusBarDarkFont() = true

    override fun initStatusBarColor() = R.color.status_bar

}