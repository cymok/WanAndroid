package com.example.wan.android.others.third

import android.os.Bundle
import com.example.wan.android.base.activity.BaseActivity
import com.example.wan.android.databinding.ActivityThirdBinding

class ThirdActivity : BaseActivity() {

    private val binding by lazy {
        ActivityThirdBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(binding.container.id, ThirdFragment.newInstance())
                .commitNow()
        }

    }

}