package com.example.flamingo.index.web

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.webkit.WebChromeClient
import android.webkit.WebView
import com.blankj.utilcode.util.ActivityUtils
import com.blankj.utilcode.util.ClipboardUtils
import com.example.flamingo.App
import com.example.flamingo.R
import com.example.flamingo.base.activity.VVMBaseActivity
import com.example.flamingo.data.BannerItem
import com.example.flamingo.data.DataX
import com.example.flamingo.data.WebData
import com.example.flamingo.databinding.ActivityBaseWebBinding
import com.example.flamingo.utils.getViewModel
import com.example.flamingo.utils.gone
import com.example.flamingo.utils.toast
import com.example.flamingo.utils.toastLong
import com.lxj.xpopup.XPopup

class WebActivity : VVMBaseActivity<WebViewModel, ActivityBaseWebBinding>() {

    companion object {

        fun start(id: Int, url: String, title: String? = null) {
            ActivityUtils.startActivity(
                Intent(App.INSTANCE, WebActivity::class.java).apply {
                    putExtra(
                        "data",
                        WebData(
                            id = id,
                            url = url,
                            title = title,
                        )
                    )
                }
            )
        }

        fun start(dataX: DataX) {
            ActivityUtils.startActivity(
                Intent(App.INSTANCE, WebActivity::class.java).apply {
                    putExtra(
                        "data",
                        WebData(
                            id = dataX.id,
                            url = dataX.link,
                            title = dataX.title,
                        )
                    )
                }
            )
        }

        fun start(bannerItem: BannerItem) {
            ActivityUtils.startActivity(
                Intent(App.INSTANCE, WebActivity::class.java).apply {
                    putExtra(
                        "data",
                        WebData(
                            id = bannerItem.id,
                            url = bannerItem.url,
                            title = bannerItem.title,
                        )
                    )
                }
            )
        }

    }

    lateinit var webData: WebData

    override val binding by lazy { ActivityBaseWebBinding.inflate(layoutInflater) }

    override val viewModel: WebViewModel get() = getViewModel()

    override fun viewModelObserve() {
        super.viewModelObserve()
        viewModel.like.observe(this) {
            if (it) {
                toast("已收藏")
                menu.findItem(R.id.menu_item_like).setIcon(R.drawable.icon_star_selected)
            } else {
                toast("已取消收藏")
                menu.findItem(R.id.menu_item_like).setIcon(R.drawable.icon_star)
            }
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
                    supportActionBar?.title = webData.title ?: title ?: "文章"
                }
            }
            loadUrl(webData.url)
        }
    }

    override fun initStatusBarColor() = R.color.primary

    lateinit var menu: Menu

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        this.menu = menu
        menuInflater.inflate(R.menu.menu_web_activity, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_item_like -> {
                val like = viewModel.like.value ?: false
                if (like) {
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