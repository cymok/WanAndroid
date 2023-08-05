package com.example.flamingo.index.splash

import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import com.example.flamingo.base.activity.BaseActivity
import com.example.flamingo.databinding.ActivitySplashBinding
import com.example.flamingo.index.home.HomeActivity
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

class SplashActivity : BaseActivity() {
    companion object {
//        const val COUNTDOWN_TIME = 3
        const val COUNTDOWN_TIME = 0
    }

    private val binding by lazy {
        ActivitySplashBinding.inflate(layoutInflater)
    }

    private var mCountDown: Job? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initCountDown()

        binding.tvSkip.onClick {
            mCountDown?.cancel()
            start<HomeActivity> {}
            finish()
        }
    }

    private fun initCountDown() {
        mCountDown = countDownByFlow(COUNTDOWN_TIME, lifecycleScope, {
            if (it == 0) mCountDown?.cancel()
            binding.tv.text = it.toString()
        }, {
            start<HomeActivity> {}
            finish()
        })
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