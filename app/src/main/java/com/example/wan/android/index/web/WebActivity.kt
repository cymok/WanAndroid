package com.example.wan.android.index.web

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Html
import android.view.Menu
import android.view.MenuItem
import android.webkit.WebChromeClient
import android.webkit.WebResourceRequest
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import com.blankj.utilcode.util.ActivityUtils
import com.blankj.utilcode.util.ClipboardUtils
import com.example.wan.android.App
import com.example.wan.android.R
import com.example.wan.android.base.activity.VBaseActivity
import com.example.wan.android.databinding.ActivityWebBinding
import com.example.wan.android.utils.toastLong
import com.example.wan.android.utils.visible

class WebActivity : VBaseActivity<ActivityWebBinding>() {

    companion object {

        fun start(
            url: String,
            title: String? = null,
        ) {
            ActivityUtils.startActivity(
                Intent(com.example.wan.android.App.INSTANCE, WebActivity::class.java).apply {
                    putExtra("url", url)
                    putExtra("title", title)
                }
            )
        }

    }

    override val binding by lazy { ActivityWebBinding.inflate(layoutInflater) }

    private val webUrl by lazy { intent.getStringExtra("url")!! }
    private val webTitle by lazy { intent.getStringExtra("title") }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportActionBar?.title = Html.fromHtml(webTitle ?: "")

        binding.webView.run {
            settings.run {
                javaScriptEnabled = true
//                javaScriptCanOpenWindowsAutomatically = true
                useWideViewPort = true  // 将图片调整到适合webview的大小

                layoutAlgorithm = WebSettings.LayoutAlgorithm.SINGLE_COLUMN
                loadWithOverviewMode = true // 缩放至屏幕的大小
                setSupportZoom(true); // 支持缩放，默认为true。是setBuiltInZoomControls(true)的前提。
                builtInZoomControls = true; // 设置内置的缩放控件。若为false=不可缩放
                displayZoomControls = false; // 隐藏原生的缩放控件

                cacheMode = WebSettings.LOAD_CACHE_ELSE_NETWORK // 缓存模式

//                allowFileAccess = true // 可以访问文件
                domStorageEnabled = true // 开启 DOM storage 例如 微信文章 需要
//                databaseEnabled = true // 开启 database
//                loadsImagesAutomatically = true // 自动加载图片
                defaultTextEncodingName = "UTF-8" // 设置编码格式

            }
            webChromeClient = object : WebChromeClient() {
                override fun onProgressChanged(view: WebView?, newProgress: Int) {
                    binding.progress.run {
                        progress = newProgress
                        visible(newProgress < 100)
                    }
                }

                override fun onReceivedTitle(view: WebView?, title: String?) {
                    supportActionBar?.title = Html.fromHtml(webTitle ?: title ?: "")
                }
            }
            webViewClient = object : WebViewClient() {
                override fun shouldOverrideUrlLoading(
                    view: WebView?,
                    request: WebResourceRequest?
                ): Boolean {
                    view?.loadUrl(request?.url.toString()) // 防止自动跳转到浏览器
                    return true
                }
            }
            loadUrl(webUrl)
        }
    }

    override fun initStatusBarColor() = R.color.status_bar

    lateinit var menu: Menu

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        this.menu = menu
        menuInflater.inflate(R.menu.menu_web_activity, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_item_refresh -> {
                binding.webView.reload()
            }

            R.id.menu_item_copy -> {
                ClipboardUtils.copyText(webUrl)
                toastLong("复制成功:\n${webUrl}")
            }

            R.id.menu_item_share -> {
                ActivityUtils.startActivity(Intent().apply {
                    action = Intent.ACTION_SEND
                    putExtra(Intent.EXTRA_TEXT, webUrl)
                    putExtra(Intent.EXTRA_TITLE, webTitle)
                    type = "text/plain"
                })
            }

            R.id.menu_item_browser -> {
                ActivityUtils.startActivity(Intent().apply {
                    action = Intent.ACTION_VIEW
                    data = Uri.parse(webUrl)
                })
            }

            else -> {

            }
        }
        return true
    }

}