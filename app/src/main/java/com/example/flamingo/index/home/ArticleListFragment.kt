package com.example.flamingo.index.home

import android.graphics.Rect
import android.os.Bundle
import android.view.View
import androidx.paging.LoadState
import by.kirich1409.viewbindingdelegate.CreateMethod
import by.kirich1409.viewbindingdelegate.viewBinding
import com.blankj.utilcode.util.LogUtils
import com.example.flamingo.base.fragment.VVMBaseFragment
import com.example.flamingo.data.ArticlesTreeItem
import com.example.flamingo.databinding.FragmentArticlesBinding
import com.example.flamingo.utils.dp2px
import com.example.flamingo.utils.getViewModel
import com.grzegorzojdana.spacingitemdecoration.Spacing
import com.grzegorzojdana.spacingitemdecoration.SpacingItemDecoration

class ArticleListFragment(val item: ArticlesTreeItem, @ArticlesDataSource.Page val whichPage: Int) :
    VVMBaseFragment<ArticleListViewModel, FragmentArticlesBinding>() {

    override val viewModel: ArticleListViewModel get() = getViewModel()
    override val binding: FragmentArticlesBinding by viewBinding(CreateMethod.INFLATE)

    private val itemDecoration = SpacingItemDecoration(
        Spacing(
            vertical = 12.dp2px,
            horizontal = 50.dp2px,
            item = Rect(),
            edges = Rect(16.dp2px, 20.dp2px, 16.dp2px, 20.dp2px),
        )
    )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
    }

    private fun initView() {
        val adapter = ArticlesPagingAdapter()
        val recyclerView = binding.rv
        recyclerView.addItemDecoration(itemDecoration)
        recyclerView.adapter = adapter

        viewModel.getArticlesWithPager(whichPage = whichPage,id = item.id).observe(viewLifecycleOwner) {
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

}