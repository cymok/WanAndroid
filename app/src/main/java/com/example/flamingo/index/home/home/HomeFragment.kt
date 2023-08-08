package com.example.flamingo.index.home.home

import android.annotation.SuppressLint
import android.graphics.Rect
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Lifecycle
import androidx.paging.LoadState
import by.kirich1409.viewbindingdelegate.CreateMethod
import by.kirich1409.viewbindingdelegate.viewBinding
import com.blankj.utilcode.util.BarUtils
import com.blankj.utilcode.util.LogUtils
import com.example.flamingo.base.fragment.VVMBaseFragment
import com.example.flamingo.constant.EventBus
import com.example.flamingo.data.ArticlePage
import com.example.flamingo.databinding.FragmentHomeBinding
import com.example.flamingo.index.home.ArticlesPagingAdapter
import com.example.flamingo.utils.dp2px
import com.example.flamingo.utils.getViewModel
import com.example.flamingo.utils.observeEvent
import com.example.flamingo.utils.toast
import com.grzegorzojdana.spacingitemdecoration.Spacing
import com.grzegorzojdana.spacingitemdecoration.SpacingItemDecoration
import com.scwang.smart.refresh.header.ClassicsHeader
import com.scwang.smart.refresh.header.TwoLevelHeader
import com.youth.banner.indicator.CircleIndicator
import java.text.SimpleDateFormat

class HomeFragment : VVMBaseFragment<HomeViewModel, FragmentHomeBinding>() {

    override val viewModel: HomeViewModel get() = getViewModel()
    override val binding: FragmentHomeBinding by viewBinding(CreateMethod.INFLATE)

    private val adapter = ArticlesPagingAdapter(ArticlePage.HOME)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initImmersion()
        initView()
        observe()
    }

    private fun initImmersion() {
        val statusBarHeight = BarUtils.getStatusBarHeight()
//        binding.rv.topPadding = statusBarHeight
    }

    private fun observe() {
        viewModel.banner.observe(viewLifecycleOwner) {
            val homeBannerAdapter = HomeBannerAdapter(it)
            binding.banner.addBannerLifecycleObserver(viewLifecycleOwner)
                .setAdapter(homeBannerAdapter)
                .setIndicator(CircleIndicator(this.requireContext()))
        }
    }

    @SuppressLint("SimpleDateFormat")
    private fun initView() {
        val recyclerView = binding.rv
        recyclerView.addItemDecoration(
            SpacingItemDecoration(
                Spacing(
                    vertical = 12.dp2px,
                    horizontal = 50.dp2px,
                    item = Rect(),
                    edges = Rect(16.dp2px, 20.dp2px, 16.dp2px, 20.dp2px),
                )
            )
        )
        recyclerView.adapter = adapter

        viewModel.getArticlesWithPager().observe(viewLifecycleOwner) {
            adapter.submitData(lifecycle, it)
//            binding.refresh.isRefreshing = false
        }

        adapter.addLoadStateListener {
            when (it.refresh) {
                is LoadState.Loading -> {

                }

                is LoadState.NotLoading -> {

                }

                is LoadState.Error -> {
                    binding.refresh.finishRefresh()
                    LogUtils.e(it.toString())
                }

                else -> {

                }
            }
        }

        binding.refresh.setRefreshHeader(TwoLevelHeader(requireContext()).apply {
            setOnTwoLevelListener { refreshLayout ->
                toast("夜市还没开始")
                // true 将会展开二楼状态 false 关闭刷新
                return@setOnTwoLevelListener false
            }
            setRefreshHeader(
                ClassicsHeader(context)
                    .setTimeFormat(SimpleDateFormat("上次更新 yyyy/MM/dd HH:mm:ss"))
            )
        })
        binding.refresh.setOnRefreshListener {
            binding.refresh.finishRefresh()
            adapter.refresh()
        }
    }

    override fun observeBus() {
        observeEvent<Int>(EventBus.HOME_TAB_CHANGE) {

        }
        observeEvent<Int>(EventBus.HOME_TAB_REFRESH) {
            val index = arguments?.getInt("homeIndex")
            if (it == index && lifecycle.currentState == Lifecycle.State.RESUMED) {
                binding.rv.smoothScrollToPosition(0)
                binding.rv.post {
                    binding.refresh.autoRefresh()
                }
            }
        }
    }

}