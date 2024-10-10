package com.example.wan.android.index.search

import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.core.view.MenuProvider
import androidx.paging.LoadState
import androidx.paging.liveData
import by.kirich1409.viewbindingdelegate.CreateMethod
import by.kirich1409.viewbindingdelegate.viewBinding
import com.blankj.utilcode.util.LogUtils
import com.example.wan.android.R
import com.example.wan.android.base.fragment.VVMBaseFragment
import com.example.wan.android.data.model.LikeData
import com.example.wan.android.data.model.WebData
import com.example.wan.android.databinding.FragmentSearchBinding
import com.example.wan.android.index.common.ArticleListPagingAdapter
import com.example.wan.android.index.common.ArticleWebActivity
import com.example.wan.android.utils.ext.visible
import com.example.wan.android.utils.getViewModel
import com.example.wan.android.utils.newIntent
import com.example.wan.android.utils.registerResultOK
import com.lxj.xpopup.XPopup
import splitties.views.onClick

class SearchFragment : VVMBaseFragment<SearchViewModel, FragmentSearchBinding>() {

    override val viewModel: SearchViewModel by lazy { getViewModel() }
    override val binding: FragmentSearchBinding by viewBinding(CreateMethod.INFLATE)

    private val adapter by lazy { ArticleListPagingAdapter() }

    private var key: String = ""
    private var isInitialized = false

    private val popupView by lazy {
        XPopup.Builder(requireContext())
            .asInputConfirm("站内搜索", "", "请输入关键字") {
                if (it.isNotBlank()) {
                    search(it)
                }
            }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        observe()

        showSearchDialog()
    }

    override fun onResume() {
        super.onResume()

    }

    private fun showSearchDialog() {
        popupView.apply {
            inputContent = key
        }.show()
    }

    private fun search(key: String) {
        this.key = key
        viewModel.getArticlesPager(key).liveData
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
            isInitialized = true

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

            // 空数据 显示空页面
            val isEmpty = it.refresh is LoadState.NotLoading &&
                    it.append.endOfPaginationReached &&
                    adapter.itemCount < 1

            binding.rv.visible(isEmpty.not())
            binding.viewEmpty.visible(isEmpty)

        }

        adapter.onLikeClick {
            viewModel.like(it, true)
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
                        isMyLike = true,
                        id = dataX.id,
                        originId = dataX.originId,
                        url = dataX.link,
                        title = dataX.title,
                        like = dataX.collect,
                        position = position,
                    )
                )
            })
        }

        binding.refresh.setOnRefreshListener {
            adapter.refresh()

            // 页面未加载时 下拉刷新结束动画
            if (isInitialized.not()) {
                binding.refresh.isRefreshing = false
            }
        }

        binding.viewEmpty.onClick {
            showSearchDialog()
        }

        createMenu()
    }

    private fun createMenu() {
        requireActivity().addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.menu_search_activity, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                if (menuItem.itemId == R.id.menu_item_search) {
                    showSearchDialog()
                }
                return true
            }
        })
    }

}