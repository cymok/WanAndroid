package com.example.wan.android.index.square

import android.os.Bundle
import android.view.View
import by.kirich1409.viewbindingdelegate.CreateMethod
import by.kirich1409.viewbindingdelegate.viewBinding
import com.blankj.utilcode.util.BarUtils
import com.example.wan.android.base.fragment.VBaseFragment
import com.example.wan.android.constant.EventBus
import com.example.wan.android.databinding.FragmentSquareMixBinding
import com.example.wan.android.index.common.VpFragmentAdapter
import com.example.wan.android.index.qa.QaFragment
import com.example.wan.android.utils.observeEvent
import com.google.android.material.tabs.TabLayoutMediator
import splitties.bundle.put
import splitties.views.topPadding

class SquareMixFragment : VBaseFragment<FragmentSquareMixBinding>() {

    override val binding: FragmentSquareMixBinding by viewBinding(CreateMethod.INFLATE)

    companion object {
        fun getInstance(isPaddingTop: Boolean) =
            SquareMixFragment().apply {
                arguments = Bundle().apply {
                    put("isPaddingTop", isPaddingTop)
                }
            }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initImmersion()
        initView()
    }

    override fun onResume() {
        super.onResume()

    }

    private fun initImmersion() {
        val isPaddingTop = arguments?.getBoolean("isPaddingTop")
        binding.tabLayout.topPadding = if (isPaddingTop == true) {
            BarUtils.getStatusBarHeight()
        } else {
            0
        }
    }

    private fun initView() {
        val nameList = listOf("广场", "问答")

        // ViewPager
        val list = listOf(SquareFragment.getInstance(false), QaFragment.getInstance(false))
        val vpAdapter = VpFragmentAdapter(this, list)
        binding.viewpager.adapter = vpAdapter
        binding.viewpager.currentItem = 0
        binding.viewpager.offscreenPageLimit = 2

        // TabLayout
        TabLayoutMediator(binding.tabLayout, binding.viewpager) { tab, position ->
            tab.text = nameList[position]
        }.attach()

    }

    override fun observeBus() {
        observeEvent<Int>(EventBus.HOME_TAB_CHANGED) {

        }
    }

}