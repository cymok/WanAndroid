package com.example.wan.android.index.splash

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import com.blankj.utilcode.util.AppUtils
import com.example.wan.android.base.activity.VBaseActivity
import com.example.wan.android.databinding.ActivitySplashBinding
import com.example.wan.android.index.MainActivity
import com.example.wan.android.utils.ext.alert
import com.example.wan.android.utils.ext.cancel
import com.example.wan.android.utils.ext.ok
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import splitties.activities.start
import splitties.views.onClick

typealias MyAppUtils = com.example.wan.android.utils.AppUtils

class SplashActivity : VBaseActivity<ActivitySplashBinding>() {
    companion object {
        const val COUNTDOWN_TIME = 3
    }

    override val binding by lazy {
        ActivitySplashBinding.inflate(layoutInflater)
    }

    private var mCountDown: Job? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        initPrivacyDialog {
            initSDKWithPrivacy {
                initView {
                    start<MainActivity> {}
                    finish()
                }
            }
        }
    }

    private fun initPrivacyDialog(afterAction: () -> Unit) {
        val isAccept = MyAppUtils.isAcceptAgreement()
        if (isAccept.not()) {
            alert("提示", "请阅读《用户协议》和《隐私政策》,\n您是否同意? (假如有的话)", false) {
                cancel("拒绝") {
                    AppUtils.exitApp()
                }
                ok("同意") {
                    MyAppUtils.acceptAgreement(true)
                    afterAction.invoke()
                }
            }.show()
        } else {
            afterAction.invoke()
        }
    }

    private fun initSDKWithPrivacy(afterAction: () -> Unit) {
        val agreed = MyAppUtils.isAcceptAgreement()
        if (agreed.not()) return

        // 在此初始化访问设备信息的SDK

        afterAction.invoke()
    }

    private fun initView(afterAction: () -> Unit) {
        initCountDown {
            afterAction.invoke()
        }
        binding.tvSkip.onClick {
            mCountDown?.cancel()
            afterAction.invoke()
        }
    }

    @SuppressLint("SetTextI18n")
    private fun initCountDown(block: () -> Unit) {
        mCountDown = countDownByFlow(COUNTDOWN_TIME, lifecycleScope, {
            if (it == 0) mCountDown?.cancel()
            binding.tv.text = it.toString()
            binding.tvSkip.text = "跳过(${it})"
        }) {
            block.invoke()
        }
    }

    /**
     * 使用Flow实现一个倒计时功能
     */
    @Suppress("SameParameterValue")
    private fun countDownByFlow(
        max: Int,
        scope: CoroutineScope,
        onTick: (Int) -> Unit,
        onFinish: (() -> Unit)? = null,
    ): Job = flow {
        for (i in max downTo 0) {
            emit(i)
            if (i != 0) delay(1000)
        }
    }.flowOn(Dispatchers.Main)
        .onEach { onTick.invoke(it) }
        .onCompletion { cause -> if (cause == null) onFinish?.invoke() }
        .launchIn(scope) //保证在一个协程中执行

}