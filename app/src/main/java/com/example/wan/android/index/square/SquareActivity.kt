package com.example.wan.android.index.square

import android.os.Bundle
import com.example.wan.android.R
import com.example.wan.android.base.activity.VBaseActivity
import com.example.wan.android.databinding.ActivityArticleListBinding

class SquareActivity : VBaseActivity<ActivityArticleListBinding>() {

    override val binding by lazy { ActivityArticleListBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) {

//            supportActionBar?.title = "广场"
            titleView.text = "广场"
            supportFragmentManager.beginTransaction()
                .replace(
                    binding.root.id,
                    SquareMixFragment.getInstance(false)
                )
                .commitNow()

        }
    }

    override fun initStatusBarColor() = R.color.status_bar

}