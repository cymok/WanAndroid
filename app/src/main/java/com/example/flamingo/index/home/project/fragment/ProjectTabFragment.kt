package com.example.flamingo.index.home.project.fragment

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
import com.example.flamingo.data.ArticlePage
import com.example.flamingo.data.ArticlesTreeItem
import com.example.flamingo.data.WebData
import com.example.flamingo.databinding.FragmentProjectTabBinding
import com.example.flamingo.index.home.project.fragment.paging.ProjectTabPagingAdapter
import com.example.flamingo.index.home.subscribe.fragment.SubscribeTabFragment
import com.example.flamingo.utils.getViewModel
import com.example.flamingo.utils.observeEvent

class ProjectTabFragment : VVMBaseFragment<ProjectTabViewModel, FragmentProjectTabBinding>() {

    override val viewModel: ProjectTabViewModel get() = getViewModel()
    override val binding: FragmentProjectTabBinding by viewBinding(CreateMethod.INFLATE)

    private val adapter by lazy { ProjectTabPagingAdapter() }

    private var homeIndex: Int = -1
    private var item: ArticlesTreeItem? = null

    companion object {
        fun getInstance(index: Int, data: ArticlesTreeItem) = SubscribeTabFragment().apply {
            arguments = Bundle().apply {
                putInt("homeIndex", index)
                putParcelable("data", data)
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        homeIndex = arguments?.getInt("homeIndex") ?: homeIndex
        item = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arguments?.getParcelable("data", ArticlesTreeItem::class.java)
        } else {
            arguments?.getParcelable("data")
        }

        initView()
    }

    private fun initView() {
        val recyclerView = binding.rv
        recyclerView.adapter = adapter

        viewModel.getArticlesWithPager(id = item?.id ?: 0)
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
        adapter.setLickListener { id, like ->
            viewModel.like(id, like)
        }

        binding.refresh.setOnRefreshListener {
            adapter.refresh()
        }
    }

    override fun observeBus() {
        observeEvent<WebData>(EventBus.UPDATE_LIKE) {
            if(it.requestPage == ArticlePage.PROJECT){
                adapter.updateLikeItem(it)
            }
        }
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