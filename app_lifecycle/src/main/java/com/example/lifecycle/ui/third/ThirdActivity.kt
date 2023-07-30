package com.example.lifecycle.ui.third

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.lifecycle.R

class ThirdActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_third)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, ThirdFragment.newInstance())
                .commitNow()
        }
    }
}