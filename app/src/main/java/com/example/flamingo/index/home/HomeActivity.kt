package com.example.flamingo.index.home

import android.graphics.Color
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.IdRes
import com.example.flamingo.R
import com.example.flamingo.base.BaseActivity
import com.example.flamingo.databinding.ActivityHomeBinding
import com.example.flamingo.databinding.ViewTabLayoutBinding
import com.example.flamingo.index.home.dashboard.DashboardFragment
import com.example.flamingo.index.home.home.HomeFragment
import com.example.flamingo.index.home.person.PersonFragment
import com.example.flamingo.index.home.square.SquareFragment
import com.example.flamingo.index.home.subscribe.SubscribeFragment
import com.example.flamingo.utils.load
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import com.google.android.material.tabs.TabLayoutMediator

class HomeActivity : BaseActivity() {

    private val fragments = listOf(
        HomeFragment(),
        DashboardFragment(),
        SquareFragment(),
        SubscribeFragment(),
        PersonFragment()
    )
    private val titles = listOf("首页", "项目", "广场", "订阅", "靓仔")
    private val tabIcons = listOf(
        R.drawable.icon_home,
        R.drawable.icon_dashboard,
        R.drawable.icon_squar,
        R.drawable.icon_subscribe,
        R.drawable.icon_person
    )
    private val tabSelectedIcons = listOf(
        R.drawable.icon_home_selected,
        R.drawable.icon_dashboard_selected,
        R.drawable.icon_squar_selected,
        R.drawable.icon_subscribe_selected,
        R.drawable.icon_person_selected
    )

    private val binding by lazy {
        ActivityHomeBinding.inflate(layoutInflater)
    }

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
        viewpager.offscreenPageLimit = 2

        tabLayout.addOnTabSelectedListener(object : OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                val position = tab.position
                tab.customView?.let {
                    it.findViewById<ImageView>(R.id.tab_icon).load(tabSelectedIcons[position])
                    it.findViewById<TextView>(R.id.tab_text)
                        .setTextColor(Color.parseColor("#d4237a"))
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {
                val position = tab.position
                tab.customView?.let {
                    it.findViewById<ImageView>(R.id.tab_icon).load(tabIcons[position])
                    it.findViewById<TextView>(R.id.tab_text)
                        .setTextColor(Color.parseColor("#8a8a8a"))
                }
            }

            override fun onTabReselected(tab: TabLayout.Tab) {

            }
        })

        TabLayoutMediator(tabLayout, viewpager) { tab, position ->
//            tab.text = titles[position]

            val tabView = ViewTabLayoutBinding.inflate(layoutInflater)
            tabView.bind(tabIcons[position], titles[position])
            tab.customView = tabView.root

        }.attach()

    }

}

// 扩展函数 + ViewBinding = 自定义 View
fun ViewTabLayoutBinding.bind(@IdRes icon: Int, text: String) {
    this.tabIcon.load(icon)
    this.tabText.text = text
}
