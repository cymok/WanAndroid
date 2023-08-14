package com.example.flamingo.index.splash

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.lifecycleScope
import com.blankj.utilcode.util.AppUtils
import com.example.flamingo.base.activity.VBaseActivity
import com.example.flamingo.databinding.ActivitySplashBinding
import com.example.flamingo.index.home.HomeActivity
import com.example.flamingo.utils.UserUtils
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

        val isAccept = UserUtils.isAcceptAgreement()
        if (isAccept.not()) {
            AlertDialog.Builder(this)
                .setTitle("请阅读《用户协议》和《隐私政策》,\n您是否同意? (假如有的话)")
                .setNegativeButton("拒绝") { _, _ ->
                    AppUtils.exitApp()
                }
                .setPositiveButton("同意") { _, _ ->
                    UserUtils.acceptAgreement(true)
                    nextAction()
                }
                .setCancelable(false)
                .show()
        } else {
            nextAction()
        }
    }

    private fun nextAction() {
        initCountDown {
            start<HomeActivity> {}
            finish()
        }

        binding.tvSkip.onClick {
            mCountDown?.cancel()

            start<HomeActivity> {}
            finish()
        }
    }

    override fun initStatusBarDarkFont() = true

    @SuppressLint("SetTextI18n")
    private fun initCountDown(block: () -> Unit) {
        mCountDown = countDownByFlow(COUNTDOWN_TIME, lifecycleScope, {
            if (it == 0) mCountDown?.cancel()
            binding.tv.text = it.toString()
            binding.tvSkip.text = "跳过(${it})"
        }, block)
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
    ): Job {
        return flow {
            for (i in max downTo 0) {
                emit(i)
                if (i != 0) delay(1000)
            }
        }.flowOn(Dispatchers.Main)
            .onEach { onTick.invoke(it) }
            .onCompletion { cause -> if (cause == null) onFinish?.invoke() }
            .launchIn(scope) //保证在一个协程中执行
    }

}