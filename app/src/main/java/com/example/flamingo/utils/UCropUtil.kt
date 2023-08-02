package com.example.flamingo.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.yalantis.ucrop.UCrop
import java.io.File
import kotlin.random.Random

object UCropUtil {

    private lateinit var cropPictureUri: Uri
    private var resultAction: ((Uri?) -> Unit)? = null

    @JvmStatic
    fun crop(activity: Activity, inUri: Uri, outUri: Uri? = null, resultAction: ((Uri?) -> Unit)) {
        options(activity, inUri, outUri, resultAction).start(activity)
    }

    @JvmStatic
    fun crop(fragment: Fragment, inUri: Uri, outUri: Uri? = null, resultAction: ((Uri?) -> Unit)) {
        val context = fragment.requireContext()
        options(context, inUri, outUri, resultAction).start(context, fragment)
    }

    private fun options(context: Context, inUri: Uri, outUri: Uri? = null, resultAction: ((Uri?) -> Unit)): UCrop {
        cropPictureUri = outUri
            ?: let {
                val fileName = "crop_${inUri.getFile()?.name ?: "${Random.nextLong()}"}"
                val file = File(context.externalCacheDir, fileName)
                file.getUri()
            }

        this.resultAction = resultAction
        val options = UCrop.of(inUri, cropPictureUri)
            .withAspectRatio(1f, 1f)
            .withMaxResultSize(512, 512)
            .withOptions(UCrop.Options().apply {
                setStatusBarColor(0)
                setCompressionFormat(Bitmap.CompressFormat.JPEG)
                setCompressionQuality(100)
                setCircleDimmedLayer(true)
            })
        return options
    }

    @JvmStatic
    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == UCrop.REQUEST_CROP) {
            if (resultCode == AppCompatActivity.RESULT_OK) {
                val uri = data?.data ?: cropPictureUri
                resultAction?.invoke(uri)
            } else {
                // 取消
            }
        }
    }

}