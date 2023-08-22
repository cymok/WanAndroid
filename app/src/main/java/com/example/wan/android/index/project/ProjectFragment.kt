package com.example.wan.android.index.project

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import by.kirich1409.viewbindingdelegate.CreateMethod
import by.kirich1409.viewbindingdelegate.viewBinding
import com.blankj.utilcode.util.BarUtils
import com.example.wan.android.base.fragment.VVMBaseFragment
import com.example.wan.android.constant.EventBus
import com.example.wan.android.data.ArticlesTreeItem
import com.example.wan.android.databinding.FragmentProjectBinding
import com.example.wan.android.index.common.VpFragmentAdapter
import com.example.wan.android.index.project.fragment.ProjectTabFragment
import com.example.wan.android.utils.TextUtils.htmlDecode
import com.example.wan.android.utils.getViewModel
import com.example.wan.android.utils.observeEvent
import com.google.android.material.tabs.TabLayoutMediator
import splitties.bundle.put
import splitties.views.topPadding

class ProjectFragment : VVMBaseFragment<ProjectViewModel, FragmentProjectBinding>() {

    override val viewModel: ProjectViewModel get() = getViewModel()
    override val binding: FragmentProjectBinding by viewBinding(CreateMethod.INFLATE)

    companion object {
        fun getInstance(isPaddingTop: Boolean) =
            ProjectFragment().apply {
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
            val finalList = mutableListOf<ArticlesTreeItem?>().apply {
                add(null) // 最新项目
                addAll(it)
            }

            // ViewPager
            val list = finalList.map { ProjectTabFragment.getInstance(it) }
            val vpAdapter = VpFragmentAdapter(this, list)
            binding.viewpager.adapter = vpAdapter
            binding.viewpager.currentItem = 0
            binding.viewpager.offscreenPageLimit = 2

            // TabLayout
            val nameList = finalList.map { it?.name?.htmlDecode() ?: "最新项目" }
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