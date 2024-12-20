package com.example.wan.android.utils

import android.graphics.PixelFormat
import android.graphics.Point
import android.view.Gravity
import android.view.View
import android.view.Window
import android.view.WindowManager
import com.blankj.utilcode.util.ScreenUtils
import kotlin.math.roundToInt

object FloatViewHelper {

    /**
     * 注意 addView 内存泄漏
     */
    fun showInWindow(window: Window, view: View, loc: Point? = null, sizeDp: Int? = null) {
        if (view.isAttachedToWindow) {
            return
        }
        val params = WindowManager.LayoutParams().apply {
            token = window.decorView.windowToken
            type = WindowManager.LayoutParams.TYPE_APPLICATION_ATTACHED_DIALOG
            flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL or
                    WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
            gravity = Gravity.START or Gravity.TOP // 窗口位置
            format = PixelFormat.RGBA_8888 // 窗口透明
            // View 宽高
            width = (sizeDp ?: 100).dp2pxInt
            height = (sizeDp ?: 100).dp2pxInt
            // View xy 坐标
            x = loc?.x ?: (ScreenUtils.getScreenWidth() - width)
            y = loc?.y ?: (ScreenUtils.getScreenHeight() * (3 / 4f) - height / 2f).roundToInt()
        }
        window.windowManager.addView(view, params)
    }

}