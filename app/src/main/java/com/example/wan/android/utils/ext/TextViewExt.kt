package com.example.wan.android.utils.ext

import android.graphics.Typeface
import android.widget.TextView

@Suppress("NOTHING_TO_INLINE")
inline fun TextView.setTypeface(nameFromAssets: String) {
    this.typeface = Typeface.createFromAsset(context.assets, nameFromAssets)
}
