package com.example.wan.android

import android.app.AlertDialog
import android.content.Context
import android.graphics.Color
import android.os.Build
import android.view.Gravity
import android.view.View
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import com.example.wan.android.utils.dp2px
import java.text.SimpleDateFormat
import java.util.Locale

class AppDetailDialog(context: Context?, var env: String = "", var uid: String = "") :
    AlertDialog(context) {

    override fun show() {
        setView(getCustomView())
        super.show()
    }

    private fun getCustomView(): View {
        return LinearLayout(context).also { outLL ->
            outLL.orientation = LinearLayout.VERTICAL
            outLL.layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT,
            )
            outLL.gravity = Gravity.CENTER
            outLL.setPadding(10.dp2px, 0, 10.dp2px, 0)
            outLL.addView(ScrollView(context).apply {
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                ).apply {
                    weight = 1f
                }
                addView(LinearLayout(context).apply {
                    layoutParams = LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                    )
                    addView(TextView(context).apply {
                        layoutParams = LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT,
                        )
                        gravity = Gravity.START
                        setPadding(16.dp2px, 16.dp2px, 16.dp2px, 16.dp2px)
                        val text = getInfo()
                        this.text = text
                    })
                })
            })
            outLL.addView(LinearLayout(context).also { btnLL ->
                btnLL.orientation = LinearLayout.VERTICAL
                btnLL.layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                )
                btnLL.gravity = Gravity.CENTER
                btnLL.addView(View(context).apply {
                    layoutParams = LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        1,
                    )
                    setBackgroundColor(Color.GRAY)
                })
                btnLL.addView(TextView(context).apply {
                    layoutParams = LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                    )
                    gravity = Gravity.CENTER
                    setPadding(16.dp2px, 16.dp2px, 16.dp2px, 16.dp2px)
                    text = "确定"
                    setOnClickListener {
                        dismiss()
                    }
                })
            })
        }
    }

    private fun getInfo(): String {
        // @formatter:off
        return """
            - App -
            [id${"\t\t\t\t"}] ${BuildConfig.APPLICATION_ID}
            [ver${"\t\t\t"}] ${BuildConfig.VERSION_NAME}_${BuildConfig.VERSION_CODE}
            [flavor] ${BuildConfig.FLAVOR}${if (BuildConfig.DEBUG) "Debug" else "Release"}
            [env${"\t\t\t"}] $env
            [uid${"\t\t\t"}] $uid
            - App.Build -
            [commit] ${BuildConfig.COMMIT_ID}
            [java${"\t\t"}] ${null}
            [kotlin] ${null}
            [flutter] ${null}
            [arch${"\t\t"}] ${BuildConfig.OS_ARCH}
            [host${"\t\t"}] ${BuildConfig.OS_NAME}
            [by${"\t\t\t\t"}] ${BuildConfig.USER_NAME}
            [time${"\t\t"}] ${SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(BuildConfig.BUILD_TIME.toLong())}
            - System -
            [abi${"\t\t\t"}] ${Build.SUPPORTED_ABIS.joinToString(", ")}
            [bands${"\t"}] ${Build.BRAND}
            [model${"\t"}] ${Build.MODEL}
            [ver${"\t\t\t"}] Android ${Build.VERSION.RELEASE}
            [api${"\t\t\t"}] ${Build.VERSION.SDK_INT}
            [lang${"\t\t"}] ${Locale.getDefault().language}
            - System.Build -
            [host${"\t\t"}] ${Build.HOST}
            [by${"\t\t\t\t"}] ${Build.USER}
            [time${"\t\t"}] ${SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Build.TIME)}
        """.trimIndent()
        // @formatter:on
    }

}
