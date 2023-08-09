package com.example.flamingo.index.aticle

import android.content.Intent
import android.os.Bundle
import com.blankj.utilcode.util.ActivityUtils
import com.example.flamingo.App
import com.example.flamingo.R
import com.example.flamingo.base.activity.VBaseActivity
import com.example.flamingo.data.ArticlePage
import com.example.flamingo.databinding.ActivityArticlesBinding

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

            val whichPage = intent.getIntExtra("whichPage", -1)

            // todo ArticleTree 根据上一页面是哪个，在这个activity请求接口
            // todo 再去 判断用这个 fragment 还是 新建一个 带 tab 的

            supportFragmentManager.beginTransaction()
                .replace(
                    binding.root.id,
                    ArticleListFragment.getInstance(whichPage, null)
                )
                .commitNow()
        }
    }

    override fun initStatusBarDarkFont() = true

    override fun initNavBarColor() = R.color.transparent

}