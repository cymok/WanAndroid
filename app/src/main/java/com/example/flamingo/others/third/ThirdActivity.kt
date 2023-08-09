package com.example.flamingo.others.third

import android.os.Bundle
import com.example.flamingo.base.activity.BaseActivity
import com.example.flamingo.databinding.ActivityThirdBinding

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