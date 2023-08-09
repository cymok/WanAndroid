package com.example.flamingo.others.second

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.activity.result.contract.ActivityResultContract

class SecondActivityResultContract :
    ActivityResultContract<String, String?>() {

    override fun createIntent(context: Context, input: String): Intent {
        return Intent(context, SecondActivity::class.java).apply {
            putExtra("data", input)
        }
    }

    override fun parseResult(resultCode: Int, intent: Intent?): String? {
        return intent?.takeIf { resultCode == Activity.RESULT_OK }?.getStringExtra("result")
    }

}