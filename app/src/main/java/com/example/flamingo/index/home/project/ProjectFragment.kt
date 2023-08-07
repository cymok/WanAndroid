package com.example.flamingo.index.home.project

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import by.kirich1409.viewbindingdelegate.CreateMethod
import by.kirich1409.viewbindingdelegate.viewBinding
import com.blankj.utilcode.util.BarUtils
import com.example.flamingo.base.fragment.VVMBaseFragment
import com.example.flamingo.constant.EventBus
import com.example.flamingo.databinding.FragmentProjectBinding
import com.example.flamingo.index.home.ArticleListAdapter
import com.example.flamingo.index.home.ArticleListFragment
import com.example.flamingo.index.home.ArticlesDataSource
import com.example.flamingo.utils.getViewModel
import com.example.flamingo.utils.observeEvent
import com.google.android.material.tabs.TabLayoutMediator
import splitties.views.topPadding

class ProjectFragment : VVMBaseFragment<ProjectViewModel, FragmentProjectBinding>() {

    override val viewModel: ProjectViewModel get() = getViewModel()
    override val binding: FragmentProjectBinding by viewBinding(CreateMethod.INFLATE)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initImmersion()
        observe()
        initData()
    }

    private fun initImmersion() {
        val statusBarHeight = BarUtils.getStatusBarHeight()
        binding.tabLayout.topPadding = statusBarHeight
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun observe() {
        viewModel.articlesTree.observe(viewLifecycleOwner) {
            val nameList = it.map { it.name }

            // ViewPager
            val list = it.map {
                ArticleListFragment().apply {
                    arguments = Bundle().apply {
                        val index = this@ProjectFragment.arguments?.getInt("homeIndex") ?: -1
                        putInt("homeIndex", index)
                        putInt("page", ArticlesDataSource.PROJECT)
                        putParcelable("data", it)
                    }
                }
            }
            val vpAdapter = ArticleListAdapter(this, list)
            binding.viewpager.adapter = vpAdapter
            binding.viewpager.currentItem = 0
            binding.viewpager.offscreenPageLimit = 2

            // TabLayout
            TabLayoutMediator(binding.tabLayout, binding.viewpager) { tab, position ->
                tab.text = nameList[position]
            }.attach()

        }
    }

    private fun initData() {
        viewModel.getProjectTree()
    }

    override fun observeBus() {
        observeEvent<Int>(EventBus.HOME_TAB_CHANGE) {

        }
    }

}