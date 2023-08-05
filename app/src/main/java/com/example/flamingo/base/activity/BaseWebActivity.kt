package com.example.flamingo.base.activity

import android.content.Intent
import android.os.Bundle
import android.webkit.WebChromeClient
import android.webkit.WebView
import by.kirich1409.viewbindingdelegate.CreateMethod
import by.kirich1409.viewbindingdelegate.viewBinding
import com.blankj.utilcode.util.ActivityUtils
import com.example.flamingo.App
import com.example.flamingo.databinding.ActivityBaseWebBinding
import com.example.flamingo.utils.gone

class BaseWebActivity : VBaseActivity<ActivityBaseWebBinding>() {

    companion object {
        fun start(url: String, title: String?) {
            ActivityUtils.startActivity(
                Intent(App.INSTANCE, BaseWebActivity::class.java).apply {
                    putExtra("url", url)
                    putExtra("title", title)
                }
            )
        }
    }

    override val binding: ActivityBaseWebBinding by viewBinding(CreateMethod.INFLATE)

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

}