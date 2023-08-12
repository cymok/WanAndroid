package com.example.flamingo.index.qa

import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.paging.LoadState
import by.kirich1409.viewbindingdelegate.CreateMethod
import by.kirich1409.viewbindingdelegate.viewBinding
import com.blankj.utilcode.util.LogUtils
import com.example.flamingo.base.fragment.VVMBaseFragment
import com.example.flamingo.data.LikeData
import com.example.flamingo.data.WebData
import com.example.flamingo.databinding.FragmentQaBinding
import com.example.flamingo.index.common.ArticleListPagingAdapter
import com.example.flamingo.utils.getViewModel
import com.example.flamingo.utils.newIntent
import com.example.flamingo.utils.registerResultOK
import com.example.flamingo.utils.visible

class QaFragment : VVMBaseFragment<QaViewModel, FragmentQaBinding>() {

    override val viewModel: QaViewModel by lazy { getViewModel() }
    override val binding: FragmentQaBinding by viewBinding(CreateMethod.INFLATE)

    private val adapter by lazy { ArticleListPagingAdapter() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        observe()
    }

    private fun observe() {
        viewModel.getQaList().observe(viewLifecycleOwner) {
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
            launcher.launch(newIntent<QaWebActivity> {
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