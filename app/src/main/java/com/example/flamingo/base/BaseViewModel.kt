package com.example.flamingo.base

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.flamingo.constant.AppConst
import com.example.flamingo.network.api.ApiException
import com.example.flamingo.utils.UserUtils
import com.example.flamingo.utils.toast
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.async
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

typealias Block<T> = suspend (CoroutineScope) -> T
typealias Error = suspend (Exception) -> Unit
typealias Cancel = suspend (Exception) -> Unit

abstract class BaseViewModel : ViewModel(),
    // 直接将 this 赋予 MainScope
    CoroutineScope by MainScope() {

    val loadingStatus = MutableLiveData<Int>()

    val loginStatus = MutableLiveData<Boolean>()

    open fun startLoading() {
        loadingStatus.postValue(AppConst.loading)
    }

    open fun stopLoading() {
        loadingStatus.postValue(AppConst.complete)
    }

    override fun onCleared() {
        cancel()
        super.onCleared()
    }

    /**
     * 创建并执行协程
     * @param block 协程中执行
     * @param error 错误时执行
     * @param cancel 取消时只需
     * @param showErrorToast 是否弹出错误吐司
     * @return Job
     */
    protected fun launch(
        showErrorToast: Boolean = true,
        cancel: Cancel? = null,
        error: Error? = null,
        block: Block<Unit>,
    ): Job {
        return viewModelScope.launch {
            try {
                block.invoke(this)
            } catch (e: Exception) {
                when (e) {
                    is CancellationException -> {
                        stopLoading()
                        cancel?.invoke(e)
                    }

                    else -> {
                        stopLoading()
                        onError(e, showErrorToast)
                        error?.invoke(e)
                    }
                }
            }
        }
    }

    /**
     * 创建并执行协程
     * @param block 协程中执行
     * @return Deferred<T>
     */
    protected fun <T> async(block: Block<T>): Deferred<T> {
        return viewModelScope.async { block.invoke(this) }
    }

    /**
     * 取消协程
     * @param job 协程job
     */
    protected fun cancelJob(job: Job?) {
        if (job != null && job.isActive && !job.isCompleted && !job.isCancelled) {
            job.cancel()
        }
    }

    /**
     * 统一处理错误
     * @param e 异常
     * @param showErrorToast 是否显示错误吐司
     */
    protected fun onError(e: Exception, showErrorToast: Boolean = true) {
        loadingStatus.postValue(AppConst.error)
        when (e) {
            is ApiException -> {
                when (e.errorCode) {
                    -1001 -> {
                        // 未登录
                        if (showErrorToast) {
                            toast(e.message)
                        }
                        loginStatus.postValue(false)
                        UserUtils.clear()
                    }
                    // 其他错误
                    else -> {
                        if (showErrorToast) {
                            toast(e.message)
                        }
                    }
                }
            }

            is ConnectException,
            is SocketTimeoutException,
            is UnknownHostException,
            is HttpException ->
                if (showErrorToast) toast("网络请求失败")

            else ->
                if (showErrorToast) toast(e.message ?: return)
        }
    }

}