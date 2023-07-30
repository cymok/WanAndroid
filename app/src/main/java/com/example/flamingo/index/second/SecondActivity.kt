package com.example.flamingo.index.second

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.window.OnBackInvokedDispatcher
import androidx.activity.addCallback
import androidx.appcompat.app.AppCompatActivity
import com.example.flamingo.databinding.ActivitySecondBinding

class SecondActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivitySecondBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(binding.container.id, SecondFragment.newInstance())
                .commitNow()
        }

        val data = intent.getStringExtra("data")

        binding.tv.text = "接收到 data: $data"

        if (Build.VERSION.SDK_INT >= 33) {
            onBackInvokedDispatcher.registerOnBackInvokedCallback(
                OnBackInvokedDispatcher.PRIORITY_DEFAULT
            ) {
                exitOnBackPressed()
            }
        } else {
            onBackPressedDispatcher.addCallback(this) {
                exitOnBackPressed()
            }
        }
    }

    // replace onBackPressed
    private fun exitOnBackPressed() {
        setResult(RESULT_OK, Intent().apply {
            putExtra("result", "<<<")
        })
        finish()
    }

    /*
        override fun onBackPressed() {
            setResult(RESULT_OK, Intent().apply {
                putExtra("result", "qaq")
            })
            finish()
    //        super.onBackPressed()
            onBackPressedDispatcher.onBackPressed() // replace super
        }
    */

}