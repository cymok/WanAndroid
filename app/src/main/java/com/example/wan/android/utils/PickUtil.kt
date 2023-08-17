/*
package com.example.wan.android.utils

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.blankj.utilcode.util.ToastUtils
import com.zhihu.matisse.Matisse
import com.zhihu.matisse.MimeType
import com.zhihu.matisse.engine.impl.GlideEngine
import com.zhihu.matisse.filter.Filter
import com.zhihu.matisse.internal.entity.IncapableCause
import com.zhihu.matisse.internal.entity.Item

object PickUtil {

    private const val REQUEST_CODE_CHOOSE = 10086
    private var pickResult: ((List<Uri>) -> Unit)? = null

    fun pick(fragment: Fragment, pickResult: (List<Uri>) -> Unit) {
        val permissions = if (Build.VERSION.SDK_INT >= 33) {
            Manifest.permission.READ_MEDIA_IMAGES
        } else {
            Manifest.permission.READ_EXTERNAL_STORAGE
        }
        fragment.reqPermissions(listOf(permissions)) {
            Matisse.from(fragment).pick()
        }
        this.pickResult = pickResult
    }

    fun pick(activity: FragmentActivity, pickResult: (List<Uri>) -> Unit) {
        val permissions = if (Build.VERSION.SDK_INT >= 33) {
            Manifest.permission.READ_MEDIA_IMAGES
        } else {
            Manifest.permission.READ_EXTERNAL_STORAGE
        }
        activity.reqPermissions(listOf(permissions)) {
            Matisse.from(activity).pick()
        }
        this.pickResult = pickResult
    }

    private fun Matisse.pick() {
        try {
            this
                .choose(MimeType.ofAll())
                .countable(true)
                .maxSelectable(9)
                .addFilter(object : Filter() {
                    override fun constraintTypes(): MutableSet<MimeType> {
                        return mutableSetOf<MimeType>().apply {
                            add(MimeType.JPEG)
                            add(MimeType.PNG)
                            add(MimeType.GIF)
                        }
                    }

                    override fun filter(context: Context?, item: Item?): IncapableCause? {
                        if (item == null) {
                            return IncapableCause("无效文件")
                        } else if (item.size <= 1) {
                            return IncapableCause("无效文件")
                        } else {
                            return null
                        }
                    }
                })
                //            .addFilter(GifSizeFilter(320, 320, 5 * Filter.K * Filter.K))
                //            .gridExpectedSize(resources.getDimensionPixelSize(R.dimen.grid_expected_size))
                .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
                .thumbnailScale(0.85f)
                .imageEngine(GlideEngine())
                //            .showPreview(false) // Default is `true`
                .forResult(REQUEST_CODE_CHOOSE)
        } catch (e: Exception) {
            ToastUtils.showLong(e.message)
        }
    }

    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_CODE_CHOOSE) {
            if (resultCode == AppCompatActivity.RESULT_OK) {
                val selected = Matisse.obtainResult(data)
                pickResult?.invoke(selected)
            } else {
                toast("cancel")
            }
        }
    }

}
*/
