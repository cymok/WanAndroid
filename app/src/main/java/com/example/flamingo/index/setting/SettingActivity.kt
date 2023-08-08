package com.example.flamingo.index.setting

import android.os.Bundle
import android.os.SystemClock
import com.example.flamingo.AppDetailDialog
import com.example.flamingo.R
import com.example.flamingo.base.activity.VVMBaseActivity
import com.example.flamingo.databinding.ActivitySettingBinding
import com.example.flamingo.utils.UserUtils
import com.example.flamingo.utils.getViewModel
import com.example.flamingo.utils.toast
import com.lxj.xpopup.XPopup
import splitties.views.onClick

class SettingActivity : VVMBaseActivity<SettingViewModel, ActivitySettingBinding>() {

    override val viewModel: SettingViewModel by lazy { getViewModel() }
    override val binding by lazy { ActivitySettingBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
    }

    override fun initStatusBarColor() = R.color.status_bar

    private fun initView() {
        binding.llCache.onClick {}
        binding.llSkgin.onClick {}
        binding.llSource.onClick {}
        binding.llVersion.onClick {
            onMultiClick {
                AppDetailDialog(
                    context = activity,
                    env = "null",
                    uid = UserUtils.getUserInfo()?.username ?: "null",
                ).show()
            }
        }
        binding.llLogout.onClick {
            XPopup.Builder(this)
                .asConfirm("提示", "要注销登录吗?") {
                    viewModel.logout()
                }.show()
        }
    }

    override fun viewModelObserve() {
        super.viewModelObserve()
        viewModel.logStatus.observe(this) {
            if (it.not()) {
                toast("已注销登录")
                setResult(RESULT_OK)
                finish()
            }
        }
    }

    private val size = 5
    private val time = 200 * size
    private var mHints: Array<Long?> = arrayOfNulls(size)

    private fun onMultiClick(listener: () -> Unit) {
        System.arraycopy(mHints, 1, mHints, 0, mHints.size - 1)//每次点击时，数组向前移动一位
        mHints[mHints.size - 1] = SystemClock.uptimeMillis()//为数组最后一位赋值
        if (SystemClock.uptimeMillis() - (mHints[0] ?: 0L) <= time) {//连续点击之间有效间隔
            mHints = arrayOfNulls(size)
            listener()
        }
    }

}