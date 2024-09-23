package com.example.wan.android.index.web

import android.content.Intent
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
import com.example.wan.android.utils.ext.visible
import com.example.wan.android.utils.startBrowser
import com.example.wan.android.utils.toastLong

class WebActivity : VBaseActivity<ActivityWebBinding>() {

    companion object {

        fun start(
            url: String,
            title: String? = null,
        ) {
            ActivityUtils.startActivity(
                Intent(App.INSTANCE, WebActivity::class.java).apply {
                    putExtra("url", url)
                    putExtra("title", title)
                }
            )
        }

    }

    override val binding by lazy { ActivityWebBinding.inflate(layoutInflater) }

    private val webUrl by lazy { intent.getStringExtra("url")!! }
    private val webTitle by lazy { intent.getStringExtra("title") }

    override fun onDestroy() {
        // 解决 WebView 内存泄漏 2/2
        webView.stopLoading()
        webView.destroy()
        binding.layoutWebViewContainer.removeAllViews()
        super.onDestroy()
    }

    private val webView by lazy {
        // 解决 WebView 内存泄漏 1/2
        WebView(App.INSTANCE).also {
            binding.layoutWebViewContainer.addView(it)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        supportActionBar?.title = Html.fromHtml(webTitle ?: "")
        titleView.text = Html.fromHtml(webTitle ?: "")

        webView.run {
            settings.run {
                javaScriptEnabled = true
//                javaScriptCanOpenWindowsAutomatically = true
                useWideViewPort = true  // 将图片调整到适合webview的大小

                layoutAlgorithm = WebSettings.LayoutAlgorithm.SINGLE_COLUMN
                loadWithOverviewMode = true // 缩放至屏幕的大小
                setSupportZoom(true); // 支持缩放，默认为true。是setBuiltInZoomControls(true)的前提。
                builtInZoomControls = true; // 设置内置的缩放控件。若为false=不可缩放
                displayZoomControls = false; // 隐藏原生的缩放控件

                /*
                - **`LOAD_DEFAULT`**：默认的缓存使用模式。
                - **`LOAD_CACHE_ELSE_NETWORK`**：如果缓存可用，则使用缓存；否则从网络加载。
                - **`LOAD_NO_CACHE`**：不使用缓存，每次都从网络加载。
                - **`LOAD_CACHE_ONLY`**：只使用缓存，不从网络加载。
                */
                cacheMode = WebSettings.LOAD_CACHE_ELSE_NETWORK

//                allowFileAccess = true // 可以访问文件
                domStorageEnabled = true // 开启 DOM 存储 例如 微信文章 需要
                databaseEnabled = true // 开启 database 数据库存储
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
                    titleView.text = Html.fromHtml(/*webTitle ?: */title ?: "")
                }
            }
            webViewClient = object : WebViewClient() {
                override fun shouldOverrideUrlLoading(
                    view: WebView,
                    request: WebResourceRequest
                ): Boolean {
                    // Return `true` to cancel the current load
                    when (request.url?.scheme) {
                        "http", "https" -> {
                            // 页面跳转 使用 webView 打开 防止自动跳转到浏览器
                            view.loadUrl(request.url.toString())
                            return true
                        }

                        else -> {
                            startBrowser(request.url.toString())
                            return true
                        }
                    }
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

        val url = webView.url ?: webUrl
        val title = webView.title ?: webTitle

        when (item.itemId) {
            R.id.menu_item_refresh -> {
                webView.reload()
            }

            R.id.menu_item_copy -> {
                ClipboardUtils.copyText(url)
                toastLong("复制成功:\n${url}")
            }

            R.id.menu_item_share -> {
                startActivity(Intent().apply {
                    action = Intent.ACTION_SEND
                    putExtra(Intent.EXTRA_TEXT, url)
                    putExtra(Intent.EXTRA_TITLE, title)
                    type = "text/plain"
                })
            }

            R.id.menu_item_browser -> {
                startBrowser(url)
            }

            else -> {

            }
        }
        return true
    }

    override fun onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack()
        } else {
            super.onBackPressed()
        }
    }

}