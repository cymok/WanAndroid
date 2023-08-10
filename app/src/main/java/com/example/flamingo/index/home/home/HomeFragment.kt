package com.example.flamingo.index.home.home

import android.annotation.SuppressLint
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
import com.example.flamingo.data.Banner
import com.example.flamingo.data.LikeData
import com.example.flamingo.data.WebData
import com.example.flamingo.databinding.FragmentHomeBinding
import com.example.flamingo.index.home.home.paging.HomePagingAdapter
import com.example.flamingo.index.web.WebActivity
import com.example.flamingo.utils.getViewModel
import com.example.flamingo.utils.newIntent
import com.example.flamingo.utils.observeEvent
import com.example.flamingo.utils.registerResultOK
import splitties.views.topPadding

class HomeFragment : VVMBaseFragment<HomeViewModel, FragmentHomeBinding>() {

    override val viewModel: HomeViewModel get() = getViewModel()
    override val binding: FragmentHomeBinding by viewBinding(CreateMethod.INFLATE)

    private val adapter = HomePagingAdapter(this, Banner())

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initImmersion()
        initView()
        observe()
    }

    private fun initImmersion() {
        // 首页顶部是banner 显示内容不需要加一个状态栏的 padding
        binding.rv.topPadding = 0
    }

    private fun observe() {
        viewModel.banner.observe(viewLifecycleOwner) {
            adapter.setBanner(it)
        }
        viewModel.getArticlesWithPager().observe(viewLifecycleOwner) {
            adapter.submitData(lifecycle, it)
            binding.refresh.isRefreshing = false
        }
        viewModel.likeStatus.observe(viewLifecycleOwner) {
            adapter.notifyLikeChanged(it)
        }
    }

    @SuppressLint("SimpleDateFormat")
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
                        position2 = data.position2,
                    )
                )
            }
        }
        adapter.onBannerClick { position, position2, bannerItem ->
            launcher.launch(newIntent<WebActivity> {
                putExtra(
                    "data", WebData(
                        id = bannerItem.id,
                        url = bannerItem.url,
                        title = bannerItem.title,
                        like = bannerItem.collect,
                        position = position,
                        position2 = position2,
                    )
                )
            })
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
        observeEvent<Int>(EventBus.HOME_TAB_CHANGED) {

        }
        observeEvent<Int>(EventBus.HOME_TAB_REFRESH) {
            if (lifecycle.currentState == Lifecycle.State.RESUMED) {
                binding.rv.smoothScrollToPosition(0)
                binding.rv.post {
                    binding.refresh.isRefreshing = true
                    adapter.refresh()
                }
            }
        }
    }

}