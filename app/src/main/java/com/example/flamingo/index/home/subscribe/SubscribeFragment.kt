package com.example.flamingo.index.home.subscribe

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import by.kirich1409.viewbindingdelegate.CreateMethod
import by.kirich1409.viewbindingdelegate.viewBinding
import com.blankj.utilcode.util.BarUtils
import com.example.flamingo.base.fragment.VVMBaseFragment
import com.example.flamingo.constant.EventBus
import com.example.flamingo.databinding.FragmentSubscribeBinding
import com.example.flamingo.index.home.VpFragmentAdapter
import com.example.flamingo.index.home.subscribe.fragment.SubscribeTabFragment
import com.example.flamingo.utils.getViewModel
import com.example.flamingo.utils.observeEvent
import com.google.android.material.tabs.TabLayoutMediator
import splitties.bundle.put
import splitties.views.topPadding

class SubscribeFragment : VVMBaseFragment<SubscribeViewModel, FragmentSubscribeBinding>() {

    override val viewModel: SubscribeViewModel get() = getViewModel()
    override val binding: FragmentSubscribeBinding by viewBinding(CreateMethod.INFLATE)

    companion object {
        fun getInstance(isPaddingTop: Boolean) =
            SubscribeFragment().apply {
                arguments = Bundle().apply {
                    put("isPaddingTop", isPaddingTop)
                }
            }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initImmersion()
        observe()
    }

    private fun initImmersion() {
        val isPaddingTop = arguments?.getBoolean("isPaddingTop")
        binding.tabLayout.topPadding = if (isPaddingTop == true) {
            BarUtils.getStatusBarHeight()
        } else {
            0
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun observe() {
        viewModel.articlesTree.observe(viewLifecycleOwner) {
            val nameList = it.map { it.name }

            // ViewPager
            val list = it.map { item -> SubscribeTabFragment.getInstance(item) }
            val vpAdapter = VpFragmentAdapter(this, list)
            binding.viewpager.adapter = vpAdapter
            binding.viewpager.currentItem = 0
            binding.viewpager.offscreenPageLimit = 2

            // TabLayout
            TabLayoutMediator(binding.tabLayout, binding.viewpager) { tab, position ->
                tab.text = nameList[position]
            }.attach()

        }
    }

    override fun observeBus() {
        observeEvent<Int>(EventBus.HOME_TAB_CHANGED) {

        }
    }

}