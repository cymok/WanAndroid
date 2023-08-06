package com.example.flamingo.index.home.square

import android.graphics.Rect
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Lifecycle
import androidx.paging.LoadState
import by.kirich1409.viewbindingdelegate.CreateMethod
import by.kirich1409.viewbindingdelegate.viewBinding
import com.blankj.utilcode.util.LogUtils
import com.example.flamingo.base.fragment.VVMBaseFragment
import com.example.flamingo.constant.EventBus
import com.example.flamingo.databinding.FragmentSquareBinding
import com.example.flamingo.index.home.ArticlesPagingAdapter
import com.example.flamingo.utils.dp2px
import com.example.flamingo.utils.getViewModel
import com.example.flamingo.utils.observeEvent
import com.grzegorzojdana.spacingitemdecoration.Spacing
import com.grzegorzojdana.spacingitemdecoration.SpacingItemDecoration

class SquareFragment : VVMBaseFragment<SquareViewModel, FragmentSquareBinding>() {

    override val viewModel: SquareViewModel get() = getViewModel()

    // `by viewBinding()` 委托在 `onDestroyView` 里执行 `binding = null`
    override val binding by viewBinding<FragmentSquareBinding>(CreateMethod.INFLATE)

    private val adapter = ArticlesPagingAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
    }

    private fun initView() {
        val recyclerView = binding.rv
        recyclerView.adapter = adapter
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

        binding.refresh.setOnRefreshListener {
            adapter.refresh()
        }
    }

    override fun observeBus() {
        observeEvent<Int>(EventBus.HOME_TAB) {
            val index = arguments?.getInt("index")
            if (it == index && lifecycle.currentState == Lifecycle.State.RESUMED) {
                binding.rv.smoothScrollToPosition(0)
                binding.rv.post {
                    adapter.refresh()
                }
            }
        }
    }

}