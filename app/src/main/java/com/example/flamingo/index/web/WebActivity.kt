package com.example.flamingo.index.web

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.webkit.WebChromeClient
import android.webkit.WebResourceRequest
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import com.blankj.utilcode.util.ActivityUtils
import com.blankj.utilcode.util.ClipboardUtils
import com.example.flamingo.App
import com.example.flamingo.R
import com.example.flamingo.base.activity.VVMBaseActivity
import com.example.flamingo.constant.EventBus
import com.example.flamingo.data.ArticlePage
import com.example.flamingo.data.BannerItem
import com.example.flamingo.data.DataX
import com.example.flamingo.data.WebData
import com.example.flamingo.databinding.ActivityWebBinding
import com.example.flamingo.utils.getViewModel
import com.example.flamingo.utils.gone
import com.example.flamingo.utils.postEvent
import com.example.flamingo.utils.toast
import com.example.flamingo.utils.toastLong
import com.lxj.xpopup.XPopup

class WebActivity : VVMBaseActivity<WebViewModel, ActivityWebBinding>() {

    companion object {

        fun start(
            id: Int,
            url: String,
            title: String? = null,
            like: Boolean? = null,
            @ArticlePage requestPage: String? = null,
            listPosition: Int? = null,
        ) {
            ActivityUtils.startActivity(
                Intent(App.INSTANCE, WebActivity::class.java).apply {
                    putExtra(
                        "data",
                        WebData(
                            id = id,
                            url = url,
                            title = title,
                            like = like,
                            requestPage = requestPage,
                            listPosition = listPosition,
                        )
                    )
                }
            )
        }

        fun start(
            dataX: DataX,
            @ArticlePage requestPage: String? = null,
            listPosition: Int? = null,
        ) {
            ActivityUtils.startActivity(
                Intent(App.INSTANCE, WebActivity::class.java).apply {
                    putExtra(
                        "data",
                        WebData(
                            id = dataX.id,
                            url = dataX.link,
                            title = dataX.title,
                            like = dataX.collect,
                            requestPage = requestPage,
                            listPosition = listPosition,
                        )
                    )
                }
            )
        }

        fun start(
            bannerItem: BannerItem,
            @ArticlePage requestPage: String? = null,
            listPosition: Int? = null,
        ) {
            ActivityUtils.startActivity(
                Intent(App.INSTANCE, WebActivity::class.java).apply {
                    putExtra(
                        "data",
                        WebData(
                            id = bannerItem.id,
                            url = bannerItem.url,
                            title = bannerItem.title,
                            like = null,
                            requestPage = requestPage,
                            listPosition = listPosition,
                        )
                    )
                }
            )
        }

    }

    lateinit var webData: WebData

    override val binding by lazy { ActivityWebBinding.inflate(layoutInflater) }

    override val viewModel: WebViewModel get() = getViewModel()

    override fun viewModelObserve() {
        super.viewModelObserve()
        viewModel.like.observe(this) {
            if (it) {
                menu.findItem(R.id.menu_item_like).setIcon(R.drawable.icon_star_selected)
            } else {
                menu.findItem(R.id.menu_item_like).setIcon(R.drawable.icon_star)
            }
            webData.like = it
            postEvent(EventBus.UPDATE_LIKE, webData)
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

        supportActionBar?.title = webData.title ?: "文章"

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
                        if (newProgress == 100) {
                            gone()
                        }
                    }
                }

                override fun onReceivedTitle(view: WebView?, title: String?) {
                    supportActionBar?.title = webData.title ?: title ?: "文章"
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

    lateinit var menu: Menu

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        this.menu = menu
        menuInflater.inflate(R.menu.menu_web_activity, menu)

        if (webData.like == true) {
            menu.findItem(R.id.menu_item_like).setIcon(R.drawable.icon_star_selected)
        } else {
            menu.findItem(R.id.menu_item_like).setIcon(R.drawable.icon_star)
        }

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_item_like -> {
                if (webData.like == true) {
                    XPopup.Builder(this)
                        .asConfirm("提示", "您已收藏, 您要取消收藏吗?") {
                            viewModel.unlikeArticle(webData.id)
                        }.show()
                } else {
                    viewModel.likeArticle(webData.id)
                }
            }

            R.id.menu_item_refresh -> {
                binding.webView.reload()
            }

            R.id.menu_item_copy -> {
                ClipboardUtils.copyText(webData.url)
                toastLong("复制成功:\n${webData.url}")
            }

            R.id.menu_item_share -> {
                ActivityUtils.startActivity(Intent().apply {
                    action = Intent.ACTION_SEND
                    putExtra(Intent.EXTRA_TEXT, webData.url)
                    putExtra(Intent.EXTRA_TITLE, webData.title)
                    type = "text/plain"
                })
            }

            R.id.menu_item_browser -> {
                ActivityUtils.startActivity(Intent().apply {
                    action = Intent.ACTION_VIEW
                    data = Uri.parse(webData.url)
                })
            }

            else -> {

            }
        }
        return true
    }

}