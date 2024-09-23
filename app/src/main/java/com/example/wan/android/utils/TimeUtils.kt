package com.example.wan.android.utils

import java.util.Locale

fun formatSeconds(seconds: Int): String {
    val hours = seconds / 3600
    val minutes = (seconds % 3600) / 60
    val secs = seconds % 60
    return String.format(Locale.CHINESE, "%02d:%02d:%02d", hours, minutes, secs)
}

fun formatSecondsToDHMS(seconds: Int): String {
    val days = seconds / 86400
    val hours = (seconds % 86400) / 3600
    val minutes = (seconds % 3600) / 60
    val secs = seconds % 60
    return String.format(Locale.CHINESE, "%d天%02d小时%02d分钟%02d秒", days, hours, minutes, secs)
}
