package com.example.flamingo.index.home

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.graphics.Color
import android.graphics.Point
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.annotation.RawRes
import androidx.appcompat.widget.AppCompatImageView
import androidx.lifecycle.lifecycleScope
import com.blankj.utilcode.util.AppUtils
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.ScreenUtils
import com.example.flamingo.R
import com.example.flamingo.base.activity.VBaseActivity
import com.example.flamingo.constant.EventBus
import com.example.flamingo.data.ArticlePage
import com.example.flamingo.databinding.ActivityHomeBinding
import com.example.flamingo.databinding.ViewTabLayoutBinding
import com.example.flamingo.index.article.ArticleListActivity
import com.example.flamingo.index.home.home.HomeFragment
import com.example.flamingo.index.home.person.PersonFragment
import com.example.flamingo.index.home.project.ProjectActivity
import com.example.flamingo.index.home.square.SquareFragment
import com.example.flamingo.index.home.subscribe.SubscribeActivity
import com.example.flamingo.index.home.subscribe.SubscribeFragment
import com.example.flamingo.index.qa.QaActivity
import com.example.flamingo.index.qa.QaFragment
import com.example.flamingo.index.search.SearchActivity
import com.example.flamingo.utils.DraggableViewHelper
import com.example.flamingo.utils.FloatViewHelper
import com.example.flamingo.utils.UmengUtils
import com.example.flamingo.utils.UserUtils
import com.example.flamingo.utils.dp2px
import com.example.flamingo.utils.loadCircle
import com.example.flamingo.utils.loadRes
import com.example.flamingo.utils.observeEvent
import com.example.flamingo.utils.postEvent
import com.example.flamingo.utils.toast
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import com.google.android.material.tabs.TabLayoutMediator
import com.umeng.message.PushAgent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import splitties.activities.start
import splitties.views.onClick

class HomeActivity : VBaseActivity<ActivityHomeBinding>() {

    companion object {
        const val DELAY_TIME: Long = 1000
    }

    private val fragments = listOf(
        HomeFragment(),
        QaFragment.getInstance(true),
        SquareFragment(),
        SubscribeFragment.getInstance(true),
        PersonFragment(),
    )
    private val titles = listOf("推荐", "问答", "广场", "订阅", "靓仔")
    private val tabIcons = listOf(
        R.drawable.icon_home,
        R.drawable.icon_project,
        R.drawable.icon_squar,
        R.drawable.icon_subscribe,
        R.drawable.icon_conan
    )

    private val tabSelectedIcons = listOf(
        R.drawable.icon_home_selected,
        R.drawable.icon_project_selected,
        R.drawable.icon_squar_selected,
        R.drawable.icon_subscribe_selected,
        R.drawable.icon_conan_selected
    )

    override val binding by lazy { ActivityHomeBinding.inflate(layoutInflater) }

    private val floatView by lazy { AppCompatImageView(this) }

    private var secondLastIndex = -1
    private var lastIndex = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        registerUmeng()
        initView()
    }

    private fun registerUmeng() {
        val agreed = UserUtils.isAcceptAgreement()
        if (agreed) {
            // Umeng 需在用户同意隐私政策协议之后调用，否则会出现合规问题
            lifecycleScope.launch {
                withContext(Dispatchers.IO) {
                    //建议在子线程中初始化（启动优化）
                    UmengUtils.init(activity)
                }
                delay(1000)
                PushAgent.getInstance(activity).onAppStart()

                val deviceToken = UmengUtils.getDeviceToken(activity)
                LogUtils.eTag("umeng", "deviceToken = $deviceToken")

                if (deviceToken.isBlank()) {
                    // 拿不到 deviceToken 再次尝试注册
                    UmengUtils.preInit(activity)
                    withContext(Dispatchers.IO) {
                        UmengUtils.init(activity)
                    }
                    delay(1000)
                    PushAgent.getInstance(activity).onAppStart()

                    val deviceTokenAgain = UmengUtils.getDeviceToken(activity)
                    LogUtils.eTag("umeng", "deviceToken = $deviceTokenAgain")
                }
            }
        }
    }

    override fun initStatusBarDarkFont() = true

    private fun initDrawerLayout() {
        binding.viewInclude.run {
            tvSearch.onClick {
                start<SearchActivity> {}
                binding.root.close()
            }
            tvHome.onClick {
                ArticleListActivity.start(arrayListOf(ArticlePage.HOME))
                binding.root.close()
            }
            tvStudy.onClick {
                ArticleListActivity.start(arrayListOf(ArticlePage.STUDY))
                binding.root.close()
            }
            tvSquare.onClick {
                ArticleListActivity.start(arrayListOf(ArticlePage.SQUARE))
                binding.root.close()
            }
            tvNavigation.onClick {
                ArticleListActivity.start(arrayListOf(ArticlePage.NAV))
                binding.root.close()
            }
            tvTutorials.onClick {
                ArticleListActivity.start(arrayListOf(ArticlePage.TUTORIALS))
                binding.root.close()
            }
            tvQa.onClick {
//                ArticleListActivity.start(arrayListOf(ArticlePage.QA))
                start<QaActivity> {}
                binding.root.close()
            }
            tvProjects.onClick {
//                ArticleListActivity.start(arrayListOf(ArticlePage.PROJECT))
                start<ProjectActivity> {}
                binding.root.close()
            }
            tvSubscribe.onClick {
//                ArticleListActivity.start(arrayListOf(ArticlePage.SUBSCRIBE))
                start<SubscribeActivity> {}
                binding.root.close()
            }
            tvTools.onClick {
                ArticleListActivity.start(arrayListOf(ArticlePage.TOOLS))
                binding.root.close()
            }
        }
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus) {
            if (floatView.isAttachedToWindow.not()) {
                floatView.loadCircle(R.drawable.icon_conan_selected)
                val sizeDp = 75
                FloatViewHelper.showInWindow(
                    window, floatView, loc = Point(
                        (ScreenUtils.getScreenWidth() - sizeDp.dp2px),
                        (ScreenUtils.getScreenHeight() * (3 / 4f) - (sizeDp / 2f).dp2px).toInt()
                    ), sizeDp = sizeDp
                )
                DraggableViewHelper.intrude(floatView)
                floatView.onClick {
                    binding.root.run {
                        if (isOpen) {
                            close()
                        } else {
                            open()
                        }
                    }
                }
            }
        }
    }

    private fun initView() {
        initDrawerLayout()

        val viewpager = binding.viewpager
        val tabLayout = binding.tabLayout

        val adapter = HomeAdapter(this, fragments)
        viewpager.adapter = adapter
        viewpager.setCurrentItem(0, false)
        viewpager.offscreenPageLimit = 1

        viewpager.isUserInputEnabled = false

        val animatorCache = mutableMapOf<Int, Animator?>()

        tabLayout.addOnTabSelectedListener(object : OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                val position = tab.position
                tab.customView?.let {
                    it.findViewById<ImageView>(R.id.tab_icon).loadRes(tabSelectedIcons[position])
                    it.findViewById<TextView>(R.id.tab_text)
                        .setTextColor(Color.parseColor("#d4237a"))
                }
                onPageChanged(position)
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {
                val position = tab.position
                tab.customView?.let {
                    it.findViewById<ImageView>(R.id.tab_icon).loadRes(tabIcons[position])
                    it.findViewById<TextView>(R.id.tab_text)
                        .setTextColor(Color.parseColor("#8a8a8a"))
                }
            }

            override fun onTabReselected(tab: TabLayout.Tab) {
                val cacheKey = tab.customView.hashCode()

                val imageView = tab.customView!!.findViewById<ImageView>(R.id.tab_icon)
                val textView = tab.customView!!.findViewById<TextView>(R.id.tab_text)

                val position = titles.indexOf(textView.text)

                val animator = if (animatorCache[cacheKey] != null) {
                    animatorCache[cacheKey]!!
                } else {
                    ObjectAnimator.ofFloat(imageView, "rotation", 0f, -360f)
                        .apply {
                            duration = DELAY_TIME
                            addUpdateListener { animation: ValueAnimator ->
                                if (position != viewpager.currentItem) {
                                    animation.cancel()
                                    imageView.post {
                                        imageView.rotation = 0f
                                    }
                                }
                            }
                            addListener(object : AnimatorListenerAdapter() {
                                override fun onAnimationStart(animation: Animator) {
                                    // 刷新动画前切换icon
                                    imageView.loadRes(R.drawable.icon_loading)
                                }

                                override fun onAnimationEnd(animation: Animator) {
                                    // 刷新动画后还原icon
                                    if (position != viewpager.currentItem) {
                                        imageView.loadRes(tabIcons[position])
                                    } else {
                                        imageView.loadRes(tabSelectedIcons[position])
                                    }
                                }

                            })
                        }.apply {
                            animatorCache[cacheKey] = this
                        }
                }
                if (animator.isRunning.not()) {
                    animator?.start()
                    pageRefresh(position)
                }
            }
        })

        TabLayoutMediator(tabLayout, viewpager, true, false) { tab, position ->
            tab.customView = ViewTabLayoutBinding.inflate(layoutInflater).apply {
                tabText.text = titles[position]
                tabIcon.loadRes(tabIcons[position])
            }.root
        }.attach()

    }

    /**
     * 调用此方法 可以更改选中的 tab
     */
    fun changeIndex(index: Int) {
        binding.viewpager.setCurrentItem(index, false)
    }

    /**
     * tab 选中, 重复点击不会执行
     */
    fun onPageChanged(pageIndex: Int) {
        secondLastIndex = lastIndex
        lastIndex = pageIndex
        postEvent(EventBus.HOME_TAB_CHANGED, pageIndex, 500)
    }

    /**
     * tab 再次点击 通知其它页面刷新
     */
    fun pageRefresh(pageIndex: Int) {
        postEvent(EventBus.HOME_TAB_REFRESH, pageIndex)
    }

    var firstClickTime = 0L

    @Suppress("OVERRIDE_DEPRECATION")
    override fun onBackPressed() {
        val secondClickTime = System.currentTimeMillis()
        if (secondClickTime - firstClickTime > 1000) {
            toast("再次'返回'退出程序")
            firstClickTime = secondClickTime
        } else {
            AppUtils.exitApp()
        }
    }

    override fun observeBus() {
        observeEvent<Int>(EventBus.CHANGE_HOME_TAB) {
            changeIndex(secondLastIndex)
        }
    }

}

// 扩展函数 + ViewBinding = 自定义 View
fun ViewTabLayoutBinding.bind(@DrawableRes @RawRes icon: Int, text: String): View {
    this.tabIcon.loadRes(icon)
    this.tabText.text = text
    return this.root
}
