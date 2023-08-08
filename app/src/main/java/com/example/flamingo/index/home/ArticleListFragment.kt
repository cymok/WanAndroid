package com.example.flamingo.index.home

import android.graphics.Rect
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Lifecycle
import androidx.paging.LoadState
import by.kirich1409.viewbindingdelegate.CreateMethod
import by.kirich1409.viewbindingdelegate.viewBinding
import com.blankj.utilcode.util.LogUtils
import com.example.flamingo.base.fragment.VVMBaseFragment
import com.example.flamingo.constant.EventBus
import com.example.flamingo.data.ArticlesTreeItem
import com.example.flamingo.databinding.FragmentArticlesBinding
import com.example.flamingo.utils.dp2px
import com.example.flamingo.utils.getViewModel
import com.example.flamingo.utils.observeEvent
import com.grzegorzojdana.spacingitemdecoration.Spacing
import com.grzegorzojdana.spacingitemdecoration.SpacingItemDecoration
import kotlin.properties.Delegates

class ArticleListFragment : VVMBaseFragment<ArticleListViewModel, FragmentArticlesBinding>() {

    override val viewModel: ArticleListViewModel get() = getViewModel()
    override val binding: FragmentArticlesBinding by viewBinding(CreateMethod.INFLATE)

    private val adapter by lazy { ArticlesPagingAdapter(whichPage) }

    private val itemDecoration = SpacingItemDecoration(
        Spacing(
            vertical = 12.dp2px,
            horizontal = 50.dp2px,
            item = Rect(),
            edges = Rect(16.dp2px, 20.dp2px, 16.dp2px, 20.dp2px),
        )
    )

    private var homeIndex: Int = -1
    private var whichPage: Int by Delegates.notNull()
    private var item: ArticlesTreeItem? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        homeIndex = arguments?.getInt("homeIndex") ?: homeIndex
        whichPage = arguments?.getInt("whichPage")!!
        item = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arguments?.getParcelable("data", ArticlesTreeItem::class.java)
        } else {
            arguments?.getParcelable("data")
        }

        initView()
    }

    private fun initView() {
        val recyclerView = binding.rv
        recyclerView.addItemDecoration(itemDecoration)
        recyclerView.adapter = adapter

        viewModel.getArticlesWithPager(whichPage = whichPage, id = item?.id ?: 0)
            .observe(viewLifecycleOwner) {
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
        observeEvent<Int>(EventBus.HOME_TAB_CHANGED) {

        }
        observeEvent<Int>(EventBus.HOME_TAB_REFRESH) {
            if (it == homeIndex && lifecycle.currentState == Lifecycle.State.RESUMED) {
                binding.rv.smoothScrollToPosition(0)
                binding.rv.post {
                    binding.refresh.isRefreshing = true
                    adapter.refresh()
                }
            }
        }
    }

}