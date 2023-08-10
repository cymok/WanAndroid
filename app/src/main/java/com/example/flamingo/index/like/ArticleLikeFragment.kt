package com.example.flamingo.index.like

import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.paging.LoadState
import by.kirich1409.viewbindingdelegate.CreateMethod
import by.kirich1409.viewbindingdelegate.viewBinding
import com.blankj.utilcode.util.LogUtils
import com.example.flamingo.base.fragment.VVMBaseFragment
import com.example.flamingo.data.WebData
import com.example.flamingo.databinding.FragmentArticleLikeBinding
import com.example.flamingo.index.common.ArticleListPagingAdapter
import com.example.flamingo.index.web.WebActivity
import com.example.flamingo.utils.getViewModel
import com.example.flamingo.utils.newIntent
import com.example.flamingo.utils.registerResultOK

class ArticleLikeFragment : VVMBaseFragment<ArticleLikeViewModel, FragmentArticleLikeBinding>() {

    override val viewModel: ArticleLikeViewModel by lazy { getViewModel() }
    override val binding: FragmentArticleLikeBinding by viewBinding(CreateMethod.INFLATE)

    private val adapter by lazy { ArticleListPagingAdapter() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        observe()
    }

    private fun observe() {
        viewModel.getArticlesWithPager().observe(viewLifecycleOwner) {
            adapter.submitData(lifecycle, it)
            binding.refresh.isRefreshing = false
        }
        viewModel.likeStatus.observe(viewLifecycleOwner) {
            // paging 不能直接删除 adapter 的数据源
            // 更新数据
            adapter.refresh()
        }
    }

    private fun initView() {
        val recyclerView = binding.rv
        recyclerView.adapter = adapter

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

        adapter.onLikeClick {
            viewModel.like(it, true)
        }

        val launcher = registerResultOK {
            val result = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                it?.getParcelableExtra("result", WebData::class.java)
            } else {
                it?.getParcelableExtra("result")
            }
            result?.let {
                // paging 不能直接删除 adapter 的数据源
                // 更新数据
                adapter.refresh()
            }
        }
        adapter.onItemClick { position, dataX ->
            launcher.launch(newIntent<WebActivity> {
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
        }
    }

}