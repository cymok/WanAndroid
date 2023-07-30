package com.example.lifecycle

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.blankj.utilcode.util.AppUtils
import com.example.lifecycle.databinding.ActivitySecondBinding

class SecondActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivitySecondBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        lifecycle.addObserver(MyLifecycleObserver())

        binding.root.setOnClickListener {
            AppUtils.exitApp()
        }
    }
}