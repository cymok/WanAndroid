package com.example.flamingo.index.article

import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.paging.LoadState
import by.kirich1409.viewbindingdelegate.CreateMethod
import by.kirich1409.viewbindingdelegate.viewBinding
import com.blankj.utilcode.util.LogUtils
import com.example.flamingo.base.fragment.VVMBaseFragment
import com.example.flamingo.constant.EventBus
import com.example.flamingo.data.ArticlePage
import com.example.flamingo.data.ArticlesTreeItem
import com.example.flamingo.data.LikeData
import com.example.flamingo.data.WebData
import com.example.flamingo.databinding.FragmentArticlesBinding
import com.example.flamingo.index.article.paging.ArticlesPagingAdapter
import com.example.flamingo.index.web.WebActivity
import com.example.flamingo.utils.getViewModel
import com.example.flamingo.utils.newIntent
import com.example.flamingo.utils.observeEvent
import com.example.flamingo.utils.registerResultOK
import splitties.views.topPadding
import java.util.ArrayList

class ArticleListFragment private constructor(): VVMBaseFragment<ArticleListViewModel, FragmentArticlesBinding>() {

    override val viewModel: ArticleListViewModel get() = getViewModel()
    override val binding: FragmentArticlesBinding by viewBinding(CreateMethod.INFLATE)

    private val adapter by lazy { ArticlesPagingAdapter(pagePath) }

    private val item: ArticlesTreeItem? by lazy {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arguments?.getParcelable("data", ArticlesTreeItem::class.java)
        } else {
            arguments?.getParcelable("data")
        }
    }

    private val pagePath: List<String> by lazy { arguments?.getStringArrayList("pagePath")!! }

    companion object {
        fun getInstance(@ArticlePage pagePath: List<String>, data: ArticlesTreeItem? = null) =
            ArticleListFragment().apply {
                arguments = Bundle().apply {
                    putStringArrayList("pagePath", pagePath as ArrayList<String>)
                    putParcelable("data", data)
                }
            }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initImmersion()
        initView()
        observe()
    }

    private fun observe() {
        viewModel.getArticlesWithPager(pagePath = pagePath, id = item?.id ?: 0)
            .observe(viewLifecycleOwner) {
                adapter.submitData(lifecycle, it)
                binding.refresh.isRefreshing = false
            }
        viewModel.likeStatus.observe(viewLifecycleOwner) {
            adapter.notifyLikeChanged(it)
        }
    }

    private fun initImmersion() {
        binding.rv.topPadding = 0
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
            viewModel.like(it)
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
            launcher.launch(newIntent<WebActivity> {
                putExtra(
                    "data", WebData(
                        id = dataX.id,
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

    override fun observeBus() {

    }

}