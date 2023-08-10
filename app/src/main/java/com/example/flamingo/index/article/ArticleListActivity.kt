package com.example.flamingo.index.article

import android.content.Intent
import android.os.Bundle
import com.blankj.utilcode.util.ActivityUtils
import com.example.flamingo.App
import com.example.flamingo.R
import com.example.flamingo.base.activity.VBaseActivity
import com.example.flamingo.data.ArticlePage
import com.example.flamingo.databinding.ActivityArticleListBinding
import com.example.flamingo.ui.getParent
import com.example.flamingo.utils.toast

class ArticleListActivity : VBaseActivity<ActivityArticleListBinding>() {

    companion object {

        fun start(@ArticlePage pagePath: List<String>) {
            ActivityUtils.startActivity(
                Intent(App.INSTANCE, ArticleListActivity::class.java).apply {
                    putStringArrayListExtra("pagePath", pagePath as ArrayList<String>)
                }
            )
        }
    }

    override val binding by lazy { ActivityArticleListBinding.inflate(layoutInflater) }

    private val pagePath: List<String> by lazy {
        val pagePath = intent.getStringArrayListExtra("pagePath") ?: listOf<String>()
        mutableListOf<String>().apply {
            addAll(pagePath)
            add(ArticlePage.ARTICLE_LIST)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) {

            supportActionBar?.title = pagePath.getParent()

            when (pagePath.getParent()) {
                ArticlePage.HOME,
                ArticlePage.SQUARE,
                -> {
                    supportFragmentManager.beginTransaction()
                        .replace(
                            binding.root.id,
                            ArticleListFragment.getInstance(pagePath, null)
                        )
                        .commitNow()
                }

//                ArticlePage.PROJECT,
//                ArticlePage.SUBSCRIBE,
//                -> {
//                    supportFragmentManager.beginTransaction()
//                        .replace(
//                            binding.root.id,
//                            ArticleTreeFragment.getInstance(pagePath, null)
//                        )
//                        .commitNow()
//                }

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