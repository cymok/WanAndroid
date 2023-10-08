package com.example.wan.android.index

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
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.adapter.FragmentStateAdapter.FragmentTransactionCallback.OnPostEventListener
import com.blankj.utilcode.util.AppUtils
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.ScreenUtils
import com.example.wan.android.App
import com.example.wan.android.R
import com.example.wan.android.base.activity.VBaseActivity
import com.example.wan.android.constant.EventBus
import com.example.wan.android.databinding.ActivityMainBinding
import com.example.wan.android.databinding.ViewTabLayoutBinding
import com.example.wan.android.index.home.HomeActivity
import com.example.wan.android.index.home.HomeFragment
import com.example.wan.android.index.person.PersonFragment
import com.example.wan.android.index.project.ProjectActivity
import com.example.wan.android.index.project.ProjectFragment
import com.example.wan.android.index.qa.QaActivity
import com.example.wan.android.index.search.SearchActivity
import com.example.wan.android.index.square.SquareActivity
import com.example.wan.android.index.square.SquareMixFragment
import com.example.wan.android.index.subscribe.SubscribeActivity
import com.example.wan.android.index.subscribe.SubscribeFragment
import com.example.wan.android.index.web.WebActivity
import com.example.wan.android.utils.DraggableViewHelper
import com.example.wan.android.utils.FloatViewHelper
import com.example.wan.android.utils.dp2px
import com.example.wan.android.utils.ext.loadCircle
import com.example.wan.android.utils.ext.loadRes
import com.example.wan.android.utils.postEvent
import com.example.wan.android.utils.toast
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import com.google.android.material.tabs.TabLayoutMediator
import splitties.activities.start
import splitties.views.onClick

typealias MyAppUtils = com.example.wan.android.utils.AppUtils

class MainActivity : VBaseActivity<ActivityMainBinding>() {

    companion object {
        const val DELAY_TIME: Long = 1000
    }

    private val fragments = listOf(
        HomeFragment.getInstance(false),
        ProjectFragment.getInstance(true),
        SquareMixFragment.getInstance(true),
        SubscribeFragment.getInstance(true),
        PersonFragment(),
    )
    private val titles = listOf("推荐", "项目", "广场", "订阅", "我的")
    private val tabIcons = listOf(
        R.drawable.icon_home,
        R.drawable.icon_project,
        R.drawable.icon_squar,
        R.drawable.icon_subscribe,
        R.drawable.icon_person
    )

    private val tabSelectedIcons = listOf(
        R.drawable.icon_home_selected,
        R.drawable.icon_project_selected,
        R.drawable.icon_squar_selected,
        R.drawable.icon_subscribe_selected,
        R.drawable.icon_person_selected
    )

    override val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    private val floatView by lazy { AppCompatImageView(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        App.mainCreateTime = System.currentTimeMillis()
        val text = """
            启动耗时
            App.attachBaseContext: ${0}
            App.onCreate: ${App.appCreateTime - App.launchTime}
            Splash.onCreate: ${App.splashCreateTime - App.launchTime}
            Main.onCreate: ${App.mainCreateTime - App.launchTime}
        """.trimIndent()
        LogUtils.e(text)
        setContentView(binding.root)
        initSDKWithPrivacy()
        initView()
    }

    private fun initSDKWithPrivacy() {
        val agreed = MyAppUtils.isAcceptAgreement()
        if (agreed.not()) return
        // 获取用户信息的SDK 需在用户同意隐私政策协议之后调用，否则会出现合规问题

    }

    private fun initDrawerLayout() {
        binding.viewInclude.run {
            tvSearch.onClick {
                start<SearchActivity> {}
                binding.root.close()
            }
            tvHome.onClick {
                start<HomeActivity> {}
                binding.root.close()
            }
            tvStudy.onClick {
                val url = "https://wanandroid.com/route/list"
                WebActivity.start(url)
                binding.root.close()
            }
            tvSquare.onClick {
                start<SquareActivity> {}
                binding.root.close()
            }
            tvNavigation.onClick {
                val url = "https://wanandroid.com/navi"
                WebActivity.start(url)
                binding.root.close()
            }
            tvTutorials.onClick {
                val url = "https://wanandroid.com/book/list"
                WebActivity.start(url)
                binding.root.close()
            }
            tvQa.onClick {
                start<QaActivity> {}
                binding.root.close()
            }
            tvProjects.onClick {
                start<ProjectActivity> {}
                binding.root.close()
            }
            tvSubscribe.onClick {
                start<SubscribeActivity> {}
                binding.root.close()
            }
            tvTools.onClick {
                val url = "https://wanandroid.com/tools"
                WebActivity.start(url)
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
//                    binding.root.run {
//                        if (isOpen) {
//                            close()
//                        } else {
//                            open()
//                        }
//                    }
                    start<SearchActivity> {}
                }
            }
        }
    }

    private fun initView() {
        initDrawerLayout()

        val viewpager = binding.viewpager
        val tabLayout = binding.tabLayout

//        ViewPager(this).adapter = MainLazyAdapter(this, fragments)
        viewpager.adapter = MainAdapter(this, fragments).apply {
            // registerFragmentTransactionCallback 是 ViewPager2 v1.1 的 API
            registerFragmentTransactionCallback(object :
                FragmentStateAdapter.FragmentTransactionCallback() {
                override fun onFragmentMaxLifecyclePreUpdated(
                    fragment: Fragment,
                    maxLifecycleState: Lifecycle.State
                ): OnPostEventListener {
                    return if (maxLifecycleState == Lifecycle.State.RESUMED) {
                        LogUtils.e("${fragment.javaClass.simpleName} maxLifecycleState=${maxLifecycleState}")
                        OnPostEventListener {
                            // do nothing
                        }
                    } else {
                        super.onFragmentMaxLifecyclePreUpdated(fragment, maxLifecycleState)
                    }
                }

                override fun onFragmentPreAdded(fragment: Fragment): OnPostEventListener {
                    return super.onFragmentPreAdded(fragment)
                }
            })
        }
//        val pagerAdapter = MainLazyAdapter(this, fragments)
        viewpager.currentItem = 0
        viewpager.offscreenPageLimit = 2

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

        TabLayoutMediator(tabLayout, viewpager) { tab, position ->
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
        binding.viewpager.setCurrentItem(index)
    }

    /**
     * tab 选中, 重复点击不会执行
     */
    fun onPageChanged(pageIndex: Int) {
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

    }

}

// 扩展函数 + ViewBinding = 自定义 View
fun ViewTabLayoutBinding.bind(@DrawableRes @RawRes icon: Int, text: String): View {
    this.tabIcon.loadRes(icon)
    this.tabText.text = text
    return this.root
}
