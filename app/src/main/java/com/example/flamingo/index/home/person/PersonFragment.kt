package com.example.flamingo.index.home.person

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Lifecycle
import by.kirich1409.viewbindingdelegate.CreateMethod
import by.kirich1409.viewbindingdelegate.viewBinding
import com.blankj.utilcode.util.BarUtils
import com.example.flamingo.R
import com.example.flamingo.base.fragment.VVMBaseFragment
import com.example.flamingo.constant.EventBus
import com.example.flamingo.databinding.FragmentPersonBinding
import com.example.flamingo.utils.dp2px
import com.example.flamingo.utils.getViewModel
import com.example.flamingo.utils.load
import com.example.flamingo.utils.observeEvent
import com.example.flamingo.utils.postEvent

class PersonFragment : VVMBaseFragment<PersonViewModel, FragmentPersonBinding>() {

    override val viewModel: PersonViewModel get() = getViewModel()
    override val binding: FragmentPersonBinding by viewBinding(CreateMethod.INFLATE)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initImmersion()
        initView()
        observe()
    }

    private fun initData() {
        viewModel.getUserInfo()
    }

    @SuppressLint("SetTextI18n")
    private fun observe() {
        viewModel.userInfo.observe(viewLifecycleOwner) {
            binding.run {
                it.userInfo.let {
                    iv.load(
                        any = it.icon.ifBlank { R.mipmap.ic_launcher },
                        cornerRadius = 10.dp2px,
                        placeholderRes = R.mipmap.ic_launcher,
                    )
                    tvNickname.text = it.nickname
                    tvUsername.text = "用户名: ${it.username}"
                    tvId.text = "ID: ${it.id}"
                }

                it.coinInfo.let {
                    tvPoints.text = "积分: ${it.coinCount}"
                    tvRanking.text = "排行: ${it.rank}"
                }

                it.collectArticleInfo.let {
                    tvLikeNum.text = "收藏量: ${it.count}"
                }

            }
        }
    }

    private fun initView() {
//        binding.iv.loadRes(R.mipmap.ic_launcher)
    }

    private fun initImmersion() {
        val statusBarHeight = BarUtils.getStatusBarHeight()
//        binding.rv.topPadding = statusBarHeight
    }

    override fun observeBus() {
        // tab 切换时
        observeEvent<Int>(EventBus.HOME_TAB_CHANGED) {
            val index = arguments?.getInt("homeIndex")
            if (it == index && lifecycle.currentState == Lifecycle.State.RESUMED) {
                initData()
            }
        }
        // 再次点击 tab 刷新时
        observeEvent<Int>(EventBus.HOME_TAB_REFRESH) {
            val index = arguments?.getInt("homeIndex")
            if (it == index && lifecycle.currentState == Lifecycle.State.RESUMED) {
                initData()
            }
        }
    }

    override fun onCancelLogin() {
        postEvent(EventBus.CHANGE_HOME_TAB, -1)
    }

}