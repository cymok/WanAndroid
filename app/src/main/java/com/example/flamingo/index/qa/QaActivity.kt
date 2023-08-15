package com.example.flamingo.index.qa

import android.os.Bundle
import com.example.flamingo.R
import com.example.flamingo.base.activity.VBaseActivity
import com.example.flamingo.databinding.ActivityArticleListBinding

class QaActivity  : VBaseActivity<ActivityArticleListBinding>() {

    override val binding by lazy { ActivityArticleListBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) {

            supportActionBar?.title = "问答"
            supportFragmentManager.beginTransaction()
                .replace(
                    binding.root.id,
                    QaFragment()
                )
                .commitNow()

        }
    }

    override fun initStatusBarColor() = R.color.status_bar

}