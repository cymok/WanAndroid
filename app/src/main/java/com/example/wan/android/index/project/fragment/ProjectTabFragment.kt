package com.example.wan.android.index.project.fragment

import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Lifecycle
import androidx.paging.LoadState
import androidx.paging.liveData
import by.kirich1409.viewbindingdelegate.CreateMethod
import by.kirich1409.viewbindingdelegate.viewBinding
import com.blankj.utilcode.util.LogUtils
import com.example.wan.android.base.fragment.VVMBaseFragment
import com.example.wan.android.constant.EventBus
import com.example.wan.android.data.model.ArticlesTreeItem
import com.example.wan.android.data.model.LikeData
import com.example.wan.android.data.model.WebData
import com.example.wan.android.databinding.FragmentProjectTabBinding
import com.example.wan.android.index.common.ArticleListPagingAdapter
import com.example.wan.android.index.common.ArticleWebActivity
import com.example.wan.android.utils.ext.gone
import com.example.wan.android.utils.ext.visible
import com.example.wan.android.utils.getViewModel
import com.example.wan.android.utils.newIntent
import com.example.wan.android.utils.observeEvent
import com.example.wan.android.utils.registerResultOK

class ProjectTabFragment : VVMBaseFragment<ProjectTabViewModel, FragmentProjectTabBinding>() {

    override val viewModel: ProjectTabViewModel get() = getViewModel()
    override val binding: FragmentProjectTabBinding by viewBinding(CreateMethod.INFLATE)

    private val adapter by lazy { ArticleListPagingAdapter() }

    private var isRefreshing = false

    private var item: ArticlesTreeItem? = null

    companion object {
        fun getInstance(data: ArticlesTreeItem?) = ProjectTabFragment().apply {
            arguments = Bundle().apply {
                putParcelable("data", data)
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        item = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arguments?.getParcelable("data", ArticlesTreeItem::class.java)
        } else {
            arguments?.getParcelable("data")
        }

        initView()
        observe()
    }

    override fun lazyLoad() {
        viewModel.getArticlesPager(id = item?.id).liveData
            .observe(viewLifecycleOwner) {
                adapter.submitData(lifecycle, it)
                binding.refresh.isRefreshing = false
            }
    }

    private fun observe() {
        viewModel.likeStatus.observe(viewLifecycleOwner) {
            adapter.notifyLikeChanged(it)
        }
    }

    private fun initView() {
        val recyclerView = binding.rv
        recyclerView.adapter = adapter

        adapter.addLoadStateListener {
            when (it.refresh) {
                is LoadState.Loading -> {
                    if (isRefreshing.not()) {
                        binding.progressBar.visible()
                    }
                }

                is LoadState.NotLoading -> {
                    binding.progressBar.gone()
                    isRefreshing = false
                }

                is LoadState.Error -> {
                    binding.progressBar.gone()
                    isRefreshing = false
                    binding.refresh.isRefreshing = false
                    LogUtils.e(it.toString())
                }
            }
        }

        adapter.onLikeClick {
            viewModel.like(it)
        }

        val launcher = registerResultOK {
            val result = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                it?.getParcelableExtra("result", WebData::class.java)
            } else {
                it?.getParcelableExtra("result")
            }
            result?.let { data ->
                if (adapter.itemCount == 0) {
                    return@let
                }
                adapter.notifyLikeChanged(
                    LikeData(
                        id = data.id,
                        like = data.like,
                        position = data.position,
                    )
                )
            }
        }
        adapter.onItemClick { position, dataX ->
            launcher.launch(newIntent<ArticleWebActivity> {
                putExtra(
                    "data", WebData(
                        id = dataX.id,
                        url = dataX.link,
                        title = dataX.title,
                        author = dataX.author,
                        like = dataX.collect,
                        position = position,
                    )
                )
            })
        }

        binding.refresh.setOnRefreshListener {
            isRefreshing = true
            adapter.refresh()
        }
    }

    override fun observeBus() {
        observeEvent<Int>(EventBus.HOME_TAB_CHANGED) {

        }
        observeEvent<Int>(EventBus.HOME_TAB_REFRESH) {
            if (lifecycle.currentState == Lifecycle.State.RESUMED) {
                binding.rv.smoothScrollToPosition(0)
                binding.rv.post {
                    isRefreshing = true
                    binding.refresh.isRefreshing = true
                    adapter.refresh()
                }
            }
        }
    }

}