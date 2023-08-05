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
import by.kirich1409.viewbindingdelegate.CreateMethod
import by.kirich1409.viewbindingdelegate.viewBinding
import com.blankj.utilcode.util.AppUtils
import com.example.flamingo.R
import com.example.flamingo.base.activity.VBaseActivity
import com.example.flamingo.databinding.ActivityHomeBinding
import com.example.flamingo.databinding.ViewTabLayoutBinding
import com.example.flamingo.index.home.home.HomeFragment
import com.example.flamingo.index.home.person.PersonFragment
import com.example.flamingo.index.home.project.ProjectFragment
import com.example.flamingo.index.home.square.SquareFragment
import com.example.flamingo.index.home.subscribe.SubscribeFragment
import com.example.flamingo.utils.loadRes
import com.example.flamingo.utils.toast
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import com.google.android.material.tabs.TabLayoutMediator

class HomeActivity : VBaseActivity<ActivityHomeBinding>() {

    companion object {
        const val DELAY_TIME: Long = 1000
    }

    private val fragments = listOf(
        HomeFragment(),
        ProjectFragment(),
        SquareFragment(),
        SubscribeFragment(),
        PersonFragment()
    )
    private val titles = listOf("首页", "项目", "广场", "订阅", "靓仔")
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

    override val binding: ActivityHomeBinding by viewBinding(CreateMethod.INFLATE)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initView()
    }

    private fun initView() {
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
                    refreshPage(position)
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

    fun refreshPage(index: Int) {

    }

    var firstClickTime = 0L
    @Suppress("OVERRIDE_DEPRECATION")
    override fun onBackPressed() {
        val secondClickTime = System.currentTimeMillis()
        if (secondClickTime - firstClickTime > 1000) {
            toast("再次'返回'退出程序")
            firstClickTime = secondClickTime
        } else {
            AppUtils.exitApp();
        }
    }

}

// 扩展函数 + ViewBinding = 自定义 View
fun ViewTabLayoutBinding.bind(@DrawableRes @RawRes icon: Int, text: String): View {
    this.tabIcon.loadRes(icon)
    this.tabText.text = text
    return this.root
}
