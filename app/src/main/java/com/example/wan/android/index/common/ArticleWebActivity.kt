package com.example.wan.android.index.common

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.view.Menu
import android.view.MenuItem
import android.webkit.WebChromeClient
import android.webkit.WebResourceRequest
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import com.blankj.utilcode.util.ClipboardUtils
import com.example.wan.android.App
import com.example.wan.android.R
import com.example.wan.android.base.activity.VVMBaseActivity
import com.example.wan.android.data.WebData
import com.example.wan.android.databinding.ActivityWebBinding
import com.example.wan.android.utils.ext.visible
import com.example.wan.android.utils.getViewModel
import com.example.wan.android.utils.startBrowser
import com.example.wan.android.utils.toast
import com.example.wan.android.utils.toastLong
import com.lxj.xpopup.XPopup

class ArticleWebActivity : VVMBaseActivity<ArticleWebViewModel, ActivityWebBinding>() {

    lateinit var webData: WebData

    override val binding by lazy { ActivityWebBinding.inflate(layoutInflater) }

    override val viewModel: ArticleWebViewModel get() = getViewModel()

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

        val data = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra("data", WebData::class.java)
        } else {
            intent.getParcelableExtra("data")
        }
        if (data == null) {
            toast("数据有误")
            finish()
            return
        }
        this.webData = data

        supportActionBar?.title = Html.fromHtml(webData.title ?: "文章")

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
                    supportActionBar?.title = Html.fromHtml(webData.title ?: title ?: "文章")
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
            loadUrl(webData.url)
        }
    }

    override fun initStatusBarColor() = R.color.status_bar

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean("menu", true)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        if (savedInstanceState.getBoolean("menu", false)) {
            invalidateMenu() // activity 重新创建 重绘菜单
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_article_web_activity, menu)

        if (webData.like) {
            menu.findItem(R.id.menu_item_like).setIcon(R.drawable.icon_like_selected)
        } else {
            menu.findItem(R.id.menu_item_like).setIcon(R.drawable.icon_like)
        }

        viewModel.like.observe(this) {
            if (it) {
                menu.findItem(R.id.menu_item_like).setIcon(R.drawable.icon_like_selected)
            } else {
                menu.findItem(R.id.menu_item_like).setIcon(R.drawable.icon_like)
            }
            webData.like = it
        }

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_item_like -> {
                if (webData.like) {
                    XPopup.Builder(this)
                        .asConfirm("移除收藏", "《${webData.title}》") {
                            viewModel.unlikeArticle(webData.id, webData.originId, webData.isMyLike)
                        }.show()
                } else {
                    viewModel.likeArticle(webData.id, webData.originId, webData.isMyLike)
                }
            }

            R.id.menu_item_refresh -> {
                webView.reload()
            }

            R.id.menu_item_copy -> {
                ClipboardUtils.copyText(webData.url)
                toastLong("复制成功:\n${webData.url}")
            }

            R.id.menu_item_share -> {
                startActivity(Intent().apply {
                    action = Intent.ACTION_SEND
                    putExtra(Intent.EXTRA_TEXT, webData.url)
                    putExtra(Intent.EXTRA_TITLE, webData.title)
                    type = "text/plain"
                })
            }

            R.id.menu_item_browser -> {
                startBrowser(webData.url)
            }

            else -> {

            }
        }
        return true
    }

    override fun onBackPressed() {
        setResult(RESULT_OK, Intent().apply {
            putExtra("result", webData)
        })
        finish()
    }

}