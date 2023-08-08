package com.example.flamingo.index.home.person

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.Lifecycle
import by.kirich1409.viewbindingdelegate.CreateMethod
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.flamingo.R
import com.example.flamingo.base.fragment.VVMBaseFragment
import com.example.flamingo.constant.EventBus
import com.example.flamingo.data.UserInfo
import com.example.flamingo.databinding.FragmentPersonBinding
import com.example.flamingo.index.setting.SettingActivity
import com.example.flamingo.utils.UserUtils
import com.example.flamingo.utils.dp2px
import com.example.flamingo.utils.getViewModel
import com.example.flamingo.utils.load
import com.example.flamingo.utils.observeEvent
import com.example.flamingo.utils.postEvent
import com.example.flamingo.utils.toast
import splitties.views.onClick

class PersonFragment : VVMBaseFragment<PersonViewModel, FragmentPersonBinding>() {

    override val viewModel: PersonViewModel get() = getViewModel()
    override val binding: FragmentPersonBinding by viewBinding(CreateMethod.INFLATE)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        observe()
    }

    private fun initData() {
        viewModel.getUserInfo()
    }

    @SuppressLint("SetTextI18n")
    private fun observe() {
        viewModel.superUserInfo.observe(viewLifecycleOwner) {
            setUserinfo(it.userInfo)
            binding.run {
                it.coinInfo.let {
                    tvPoints.text = "积分: ${it.coinCount}"
                    tvRanking.text = "排行: ${it.rank}"
                }
                it.collectArticleInfo.let {
                    tvLikeNum.text = "${it.count} 篇"
                }
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setUserinfo(it: UserInfo) {
        binding.run {
            iv.load(
                any = it.icon.ifBlank { R.mipmap.ic_launcher },
                cornerRadius = 10.dp2px,
                placeholderRes = R.mipmap.ic_launcher,
            )
            tvNickname.text = it.nickname
            tvUsername.text = "用户名: ${it.username}"
            tvId.text = "ID: ${it.id}"
            tvEmail.text = "邮箱: ${it.email}"
        }
    }

    private fun initView() {
        val userInfo = UserUtils.getUserInfo()
        if (userInfo != null) {
            setUserinfo(userInfo)
        }

        val settingLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    // 设置页面退出登录后 原路回退
                    onCancelLogin()
                }
            }
        binding.run {
            llCoin.onClick {

            }
            llArticle.onClick {

            }
            llShared.onClick {

            }
            llSite.onClick {

            }
            llSettings.onClick {
                settingLauncher.launch(Intent(activity, SettingActivity::class.java))
            }
            llCoin.onClick {

            }
        }
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

    override fun onLoginSucceed() {
        val userInfo = UserUtils.getUserInfo()
        if (userInfo != null) {
            setUserinfo(userInfo)
        } else {
            toast("出错啦")
        }
        // 获取完整的信息
        initData()
    }

    override fun onCancelLogin() {
        postEvent(EventBus.CHANGE_HOME_TAB, -1)
    }

}