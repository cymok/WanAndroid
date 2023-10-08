package com.example.wan.android.utils

import android.app.UiModeManager
import android.content.Context
import android.content.res.Configuration
import androidx.core.content.ContextCompat

/**
 * System 暗色模式 开关
 */
val Context.sysIsDarkMode: Boolean
    get() {
        val uiModeManager = ContextCompat.getSystemService(this, UiModeManager::class.java)
            ?: return false
        return uiModeManager.nightMode == UiModeManager.MODE_NIGHT_YES
    }

/**
 * APP 最终是否 暗色模式
 */
val Context.appIsDarkMode: Boolean
    get() {
        return (this.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES
    }
