package com.example.coroutine

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.lifecycle.lifecycleScope
import com.example.coroutine.databinding.ActivityMainBinding
import com.example.utils.toast
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

class MainActivity : ComponentActivity() {

    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.root.setOnClickListener {

            runBlocking {

            }
            val handler = CoroutineExceptionHandler { context, throwable ->
                val text = "handler ${throwable.message}"
                toast(text)
            }
            lifecycleScope.launch(handler) {

                try {
                    runBlocking(lifecycleScope.coroutineContext) {
                        throw RuntimeException("test")
                    }
                } catch (e: Exception) {
                    Log.e("runBlocking", "catch Exception ${e.message}")
                }

                val job = launch(lifecycleScope.coroutineContext) {
                    try {
                        throw RuntimeException("test")
                    } catch (e: Exception) {
                        Log.e("launch", "catch Exception ${e.message}")
                    }
                }
                job.join()

                try {
                    val deferred = async(lifecycleScope.coroutineContext) {
                        throw RuntimeException("test")
                    }
                    val result = deferred.await()
                } catch (e: Exception) {
                    Log.e("async", "catch Exception ${e.message}")
                }

                try {
                    val result = withContext(lifecycleScope.coroutineContext) {
                        throw RuntimeException("test")
                    }
                } catch (e: Exception) {
                    Log.e("withContext", "withContext Exception ${e.message}")
                }

                launch(lifecycleScope.coroutineContext) {
                    toast("1")
                    delay(1000)
                    toast("111")
                    throw RuntimeException("test1")
                }
                launch(lifecycleScope.coroutineContext) {
                    toast("2")
                    delay(3000)
                    toast("222")
                    throw RuntimeException("test2")
                }.join() // 同步等待完成后才能执行后面的协程
                launch(lifecycleScope.coroutineContext) {
                    toast("3")
                    delay(2000)
                    toast("333")
                    throw RuntimeException("test3")
                }
                launch(lifecycleScope.coroutineContext) {
                    toast("4")
                    delay(1000)
                    toast("444")
                    throw RuntimeException("test4")
                }
            }

        }
    }
}