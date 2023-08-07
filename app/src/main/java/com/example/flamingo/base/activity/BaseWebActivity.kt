package com.example.flamingo.base.activity

import android.content.Intent
import android.os.Bundle
import android.webkit.WebChromeClient
import android.webkit.WebView
import com.blankj.utilcode.util.ActivityUtils
import com.blankj.utilcode.util.BarUtils
import com.blankj.utilcode.util.ColorUtils
import com.example.flamingo.App
import com.example.flamingo.R
import com.example.flamingo.databinding.ActivityBaseWebBinding
import com.example.flamingo.utils.gone

class BaseWebActivity : VBaseActivity<ActivityBaseWebBinding>() {

    companion object {
        fun start(url: String, title: String? = null) {
            ActivityUtils.startActivity(
                Intent(App.INSTANCE, BaseWebActivity::class.java).apply {
                    putExtra("url", url)
                    putExtra("title", title)
                }
            )
        }
    }

    override val binding by lazy {
        ActivityBaseWebBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val webUrl: String = intent.getStringExtra("url") ?: ""
        val webTitle: String? = intent.getStringExtra("title")

        supportActionBar?.title = webTitle ?: title ?: "文章"

        binding.webView.run {
            settings.run {
                javaScriptEnabled = true
                javaScriptCanOpenWindowsAutomatically = true
                useWideViewPort = true
            }
            webChromeClient = object : WebChromeClient() {
                override fun onProgressChanged(view: WebView?, newProgress: Int) {
                    binding.progress.run {
                        progress = newProgress
                        if (newProgress == 100) {
                            gone()
                        }
                    }
                }

                override fun onReceivedTitle(view: WebView?, title: String?) {
                    supportActionBar?.title = webTitle ?: title ?: "文章"
                }
            }
            loadUrl(webUrl)
        }
    }

    override fun initStatusBarColor() = R.color.primary

}