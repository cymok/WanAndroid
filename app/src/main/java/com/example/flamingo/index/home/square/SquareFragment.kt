package com.example.flamingo.index.home.square

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
import com.example.flamingo.data.WebData
import com.example.flamingo.databinding.FragmentSquareBinding
import com.example.flamingo.index.home.square.paging.SquarePagingAdapter
import com.example.flamingo.utils.getViewModel
import com.example.flamingo.utils.observeEvent
import splitties.views.topPadding

class SquareFragment : VVMBaseFragment<SquareViewModel, FragmentSquareBinding>() {

    override val viewModel: SquareViewModel get() = getViewModel()
    override val binding: FragmentSquareBinding by viewBinding(CreateMethod.INFLATE)

    private val adapter = SquarePagingAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initImmersion()
        initView()
    }

    private fun initImmersion() {
        val statusBarHeight = BarUtils.getStatusBarHeight()
        binding.rv.topPadding = statusBarHeight
    }

    private fun initView() {
        val recyclerView = binding.rv
        recyclerView.adapter = adapter

        viewModel.getArticlesWithPager().observe(viewLifecycleOwner) {
            adapter.submitData(lifecycle, it)
            binding.refresh.isRefreshing = false
        }

        adapter.addLoadStateListener {
            when (it.refresh) {
                is LoadState.Loading -> {

                }

                is LoadState.NotLoading -> {

                }

                is LoadState.Error -> {
                    binding.refresh.isRefreshing = false
                    LogUtils.e(it.toString())
                }

                else -> {

                }
            }
        }
        adapter.setLickListener { id, like ->
            viewModel.like(id, like)
        }

        binding.refresh.setOnRefreshListener {
            adapter.refresh()
        }
    }

    override fun observeBus() {
        observeEvent<WebData>(EventBus.UPDATE_LIKE) {
            if(it.requestPage == ArticlePage.SQUARE){
                adapter.updateLikeItem(it)
            }
        }
        observeEvent<Int>(EventBus.HOME_TAB_CHANGED) {

        }
        observeEvent<Int>(EventBus.HOME_TAB_REFRESH) {
            val index = arguments?.getInt("homeIndex")
            if (it == index && lifecycle.currentState == Lifecycle.State.RESUMED) {
                binding.rv.smoothScrollToPosition(0)
                binding.rv.post {
                    binding.refresh.isRefreshing = true
                    adapter.refresh()
                }
            }
        }
    }

}