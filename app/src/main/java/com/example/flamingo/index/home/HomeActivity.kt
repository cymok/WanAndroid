package com.example.flamingo.index.home

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.annotation.RawRes
import androidx.appcompat.widget.AppCompatImageView
import com.blankj.utilcode.util.AppUtils
import com.example.flamingo.R
import com.example.flamingo.base.activity.VBaseActivity
import com.example.flamingo.constant.EventBus
import com.example.flamingo.data.ArticlePage
import com.example.flamingo.databinding.ActivityHomeBinding
import com.example.flamingo.databinding.ViewTabLayoutBinding
import com.example.flamingo.index.article.ArticleListActivity
import com.example.flamingo.index.home.home.HomeFragment
import com.example.flamingo.index.home.person.PersonFragment
import com.example.flamingo.index.home.project.ProjectFragment
import com.example.flamingo.index.home.square.SquareFragment
import com.example.flamingo.index.home.subscribe.SubscribeFragment
import com.example.flamingo.utils.DraggableViewHelper
import com.example.flamingo.utils.FloatViewHelper
import com.example.flamingo.utils.loadCircle
import com.example.flamingo.utils.loadRes
import com.example.flamingo.utils.observeEvent
import com.example.flamingo.utils.postEvent
import com.example.flamingo.utils.toast
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import com.google.android.material.tabs.TabLayoutMediator
import splitties.bundle.put
import splitties.views.onClick

class HomeActivity : VBaseActivity<ActivityHomeBinding>() {

    companion object {
        const val DELAY_TIME: Long = 1000
    }

    private val fragments = listOf(
        HomeFragment().apply { arguments = Bundle().apply { put("homeIndex", 0) } },
        ProjectFragment().apply { arguments = Bundle().apply { put("homeIndex", 1) } },
        SquareFragment().apply { arguments = Bundle().apply { put("homeIndex", 2) } },
        SubscribeFragment().apply { arguments = Bundle().apply { put("homeIndex", 3) } },
        PersonFragment().apply { arguments = Bundle().apply { put("homeIndex", 4) } },
    )
    private val titles = listOf("推荐", "项目", "广场", "订阅", "靓仔")
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

    override val binding by lazy { ActivityHomeBinding.inflate(layoutInflater) }

    private val floatView by lazy { AppCompatImageView(this) }

    private var secondLastIndex = -1
    private var lastIndex = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        initView()
    }

    override fun initStatusBarDarkFont() = true

    private fun initDrawerLayout() {
        binding.viewInclude.run {
            tvHome.onClick {
                ArticleListActivity.start(ArticlePage.HOME)
                binding.root.close()
            }
            tvStudy.onClick {
                ArticleListActivity.start(ArticlePage.STUDY)
                binding.root.close()
            }
            tvSquare.onClick {
                ArticleListActivity.start(ArticlePage.SQUARE)
                binding.root.close()
            }
            tvNavigation.onClick {
                ArticleListActivity.start(ArticlePage.NAV)
                binding.root.close()
            }
            tvTutorials.onClick {
                ArticleListActivity.start(ArticlePage.TUTORIALS)
                binding.root.close()
            }
            tvQa.onClick {
                ArticleListActivity.start(ArticlePage.QA)
                binding.root.close()
            }
            tvProjectsHot.onClick {
                ArticleListActivity.start(ArticlePage.PROJECT_HOT)
                binding.root.close()
            }
            tvSubscribe.onClick {
                ArticleListActivity.start(ArticlePage.SUBSCRIBE)
                binding.root.close()
            }
            tvProjects.onClick {
                ArticleListActivity.start(ArticlePage.PROJECT)
                binding.root.close()
            }
            tvTools.onClick {
                ArticleListActivity.start(ArticlePage.TOOLS)
                binding.root.close()
            }
        }
    }

//    private val floatViewHelper by lazy { FloatViewHelper111() }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus) {
            if (floatView.isAttachedToWindow.not()) {
                floatView.loadCircle(R.drawable.icon_person_selected)
                FloatViewHelper.showInWindow(window, floatView, sizeDp = 75)
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

//                if (floatViewHelper.isAvailable().not())
//                    floatViewHelper.init(this, R.drawable.icon_person_selected) {
//                        binding.root.open()
//                    }
            }
        }
    }

    private fun initView() {
        initDrawerLayout()

        val viewpager = binding.viewpager
        val tabLayout = binding.tabLayout

        val adapter = HomeAdapter(this, fragments)
        viewpager.adapter = adapter
        viewpager.currentItem = 2
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
        binding.viewpager.currentItem = index
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
