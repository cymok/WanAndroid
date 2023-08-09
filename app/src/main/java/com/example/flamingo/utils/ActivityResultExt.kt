package com.example.flamingo.utils

import android.app.Activity
import android.content.Intent
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment

fun ComponentActivity.registerResult(atyResult: (result: ActivityResult) -> Unit) {
    this.registerForActivityResult(ActivityResultContracts.StartActivityForResult(), atyResult)
}

fun ComponentActivity.registerResultOK(atyResult: (Intent?) -> Unit) {
    this.registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode == Activity.RESULT_OK) {
            atyResult.invoke(it.data)
        }
    }
}

fun Fragment.registerResult(atyResult: (result: ActivityResult) -> Unit) {
    this.registerForActivityResult(ActivityResultContracts.StartActivityForResult(), atyResult)
}

fun Fragment.registerResultOK(atyResult: (Intent?) -> Unit) {
    this.registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode == Activity.RESULT_OK) {
            atyResult.invoke(it.data)
        }
    }
}
