package com.example.flamingo.index.aticle

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
import com.example.flamingo.data.WebData
import com.example.flamingo.databinding.FragmentArticlesBinding
import com.example.flamingo.index.aticle.paging.ArticlesPagingAdapter
import com.example.flamingo.utils.getViewModel
import com.example.flamingo.utils.observeEvent
import kotlin.properties.Delegates

class ArticleListFragment : VVMBaseFragment<ArticleListViewModel, FragmentArticlesBinding>() {

    override val viewModel: ArticleListViewModel get() = getViewModel()
    override val binding: FragmentArticlesBinding by viewBinding(CreateMethod.INFLATE)

    private val adapter by lazy { ArticlesPagingAdapter(whichPage) }

    private var whichPage: Int by Delegates.notNull()
    private var item: ArticlesTreeItem? = null

    companion object {
        fun getInstance(@ArticlePage whichPage: Int, data: ArticlesTreeItem? = null) =
            ArticleListFragment().apply {
                arguments = Bundle().apply {
                    putInt("whichPage", whichPage)
                    putParcelable("data", data)
                }
            }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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
        adapter.setLickListener(ArticlePage.ARTICLE_LIST) { id, like ->
            viewModel.like(id, like)
        }

        binding.refresh.setOnRefreshListener {
            adapter.refresh()
        }
    }

    override fun observeBus() {
        observeEvent<WebData>(EventBus.UPDATE_LIKE) {
            if(it.requestPage == ArticlePage.ARTICLE_LIST){
                adapter.updateLikeItem(it)
            }
        }
    }

}