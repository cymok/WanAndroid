package com.example.wan.android.index.project

import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.paging.LoadState
import by.kirich1409.viewbindingdelegate.CreateMethod
import by.kirich1409.viewbindingdelegate.viewBinding
import com.blankj.utilcode.util.LogUtils
import com.example.wan.android.base.fragment.VVMBaseFragment
import com.example.wan.android.data.LikeData
import com.example.wan.android.data.WebData
import com.example.wan.android.databinding.FragmentArticleListBinding
import com.example.wan.android.index.common.ArticleListPagingAdapter
import com.example.wan.android.index.common.ArticleWebActivity
import com.example.wan.android.utils.getViewModel
import com.example.wan.android.utils.newIntent
import com.example.wan.android.utils.registerResultOK

class NewProjectFragment : VVMBaseFragment<NewProjectViewModel, FragmentArticleListBinding>() {

    override val viewModel: NewProjectViewModel by lazy { getViewModel() }
    override val binding: FragmentArticleListBinding by viewBinding(CreateMethod.INFLATE)

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
            adapter.notifyLikeChanged(it)
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
            result?.let { data ->
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
        }
    }

}