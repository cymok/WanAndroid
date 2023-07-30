package com.example.flamingo.utils

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import com.example.utils.toast
import com.yalantis.ucrop.UCrop

object UCropUtil {

    private var destinationUri: Uri? = null
    private var resultAction: ((Uri?) -> Unit)? = null

    @JvmStatic
    fun crop(activity: Activity, inUri: Uri, outUri: Uri, resultAction: ((Uri?) -> Unit)) {
        UCrop.of(inUri, outUri)
            .withAspectRatio(1f, 1f)
            .withMaxResultSize(512, 512)
            .withOptions(UCrop.Options().apply {
                setStatusBarColor(0)
                setCompressionFormat(Bitmap.CompressFormat.JPEG)
                setCompressionQuality(100)
                setCircleDimmedLayer(true)
            })
            .start(activity)

        this.destinationUri = outUri
        this.resultAction = resultAction
    }

    @JvmStatic
    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == UCrop.REQUEST_CROP) {
            if (resultCode == AppCompatActivity.RESULT_OK) {
                val uri = data?.data ?: destinationUri
                resultAction?.invoke(uri)
            } else {
                data?.let {
                    toast("裁剪出错")
                }
            }
        }
    }

}