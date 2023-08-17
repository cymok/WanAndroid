package com.example.wan.android.others.main

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.blankj.utilcode.util.ActivityUtils
import com.blankj.utilcode.util.ConvertUtils
import com.blankj.utilcode.util.FileUtils
import com.blankj.utilcode.util.PathUtils
import com.blankj.utilcode.util.Utils
import com.bumptech.glide.Glide
import com.example.wan.android.R
import com.example.wan.android.base.activity.BaseActivity
import com.example.wan.android.others.compose.ComposeActivity
import com.example.wan.android.others.coroutine.CoroutineActivity
import com.example.wan.android.others.first.FirstActivity
import com.example.wan.android.others.second.SecondActivity
import com.example.wan.android.others.third.ThirdActivity
import com.example.wan.android.ui.compose.theme.AppTheme
import com.example.wan.android.utils.getViewModel
import com.example.wan.android.utils.toast
import kotlin.concurrent.thread

class MainActivity : BaseActivity() {

    private val pickViewModel by lazy {
        getViewModel<PickViewModel>()
    }

    private val pickObserver by lazy {
        PickObserver(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            AppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Greeting("Android")
                }
            }
        }
        observe()
    }

    private fun observe() {
        lifecycle.addObserver(pickObserver)
        pickViewModel.imageInfo.observe(this) {
            if (it.crop) {
                toast("裁剪 >>>\nuri:\n${it.uri}\npath:\n${it.path}")
            } else {
                toast("拍照 或 选取 >>>\nuri:\n${it.uri}\npath:\n${it.path}")
            }
        }
        pickViewModel.errCode.observe(this) {
            when (it) {
                PickViewModel.ERR_CANCEL -> {
                    toast("已取消")
                }

                PickViewModel.ERR_INVALID -> {
                    toast("文件无效")
                }
            }
        }
    }

    @OptIn(ExperimentalFoundationApi::class)
    @Composable
    fun Greeting(name: String) {
        val current = LocalContext.current

        val viewModel: PickViewModel = viewModel()
        val data by viewModel.imageInfo.observeAsState()

//        val scrollState = rememberScrollState(0)
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            content = {
                item {
                    Text(text = "裁剪 >>>\nuri:\n${data?.uri}\npath:\n${data?.path}")
                }
                stickyHeader {
                    AsyncImage(
                        model = data?.uri,
                        contentDescription = null,
                        modifier = Modifier
                            .size(80.dp)
                            .clip(CircleShape), // 圆形
                        contentScale = ContentScale.Crop,
                        placeholder = painterResource(id = R.drawable.ic_launcher_foreground),
                        error = painterResource(id = R.drawable.ic_launcher_foreground),
                    )
                }
                item {
                    Column(
//                    modifier = Modifier.verticalScroll(scrollState),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                    ) {
                        Divider(modifier = Modifier.height(10.dp))
                        Button(
                            onClick = {
                                val testSize = ConvertUtils.byte2FitMemorySize(123456789, 2)
                                toast(testSize)
                            },
                        ) {
                            Text(
                                text = "Test Button",
                            )
                        }
                        Divider(modifier = Modifier.height(10.dp))
                        Button(
                            onClick = {
                                pickObserver.takePicture()
                            },
                        ) {
                            Text(
                                text = "拍照",
                            )
                        }
                        Button(
                            onClick = {
                                pickObserver.pickAlbum()
                            },
                        ) {
                            Text(
                                text = "相册",
                            )
                        }
                        Button(
                            onClick = {
                                pickObserver.pickFile()
                            },
                        ) {
                            Text(
                                text = "文件",
                                textDecoration = TextDecoration.LineThrough,
                            )
                        }
                        Button(
                            onClick = {
                                pickObserver.pickMatisse()
                            },
                        ) {
                            Text(
                                text = "图库 Matisse-Compose",
                            )
                        }
                        Divider(modifier = Modifier.height(10.dp))
                        Button(
                            onClick = {
                                pickObserver.pickMatisse(false)
                            },
                        ) {
                            Text(
                                text = "不裁剪 - 图库 Matisse-Compose",
                            )
                        }
                        Button(
                            onClick = {
                                pickObserver.pickFile(false)
                            },
                        ) {
                            Text(
                                text = "不裁剪 - 文件",
                                textDecoration = TextDecoration.LineThrough,
                            )
                        }
                        Button(
                            onClick = {
                                pickObserver.pickAlbum(false)
                            },
                        ) {
                            Text(
                                text = "不裁剪 - 相册",
                            )
                        }
                        Button(
                            onClick = {
                                pickObserver.takePicture(false)
                            },
                        ) {
                            Text(
                                text = "不裁剪 - 拍照",
                            )
                        }
                        Divider(modifier = Modifier.height(10.dp))
                        Button(
                            onClick = {
                                val pkg = "com.tencent.mm"
                                val intent = try {
                                    Intent(
                                        Intent.ACTION_VIEW,
                                        Uri.parse("market://details?q=${pkg}")
                                    )
                                } catch (e: Exception) {
                                    Intent(
                                        Intent.ACTION_VIEW,
                                        Uri.parse("https://play.google.com/store/apps/details?id=${pkg}")
                                    )
                                }
                                current.startActivity(intent)
                            },
                        ) {
                            Text(
                                text = "跳转到 AppStore",
                            )
                        }
                        Divider(modifier = Modifier.height(10.dp))
                        Button(
                            onClick = {
                                // cache 目录
                                FileUtils.deleteAllInDir(PathUtils.getInternalAppCachePath())
                                FileUtils.deleteAllInDir(PathUtils.getExternalAppCachePath())
                                // glide
                                Glide.get(Utils.getApp()).clearMemory()
                                thread {
                                    Glide.get(Utils.getApp()).clearDiskCache()
                                }
                                // coil

                                // 无论成功与否 先toast成功再设置UI为0KB
                                toast("清理成功")
                            },
                        ) {
                            Text(
                                text = "清理缓存",
                            )
                        }
                        Divider(modifier = Modifier.height(10.dp))
                        Button(
                            onClick = {
                                ActivityUtils.startActivity(FirstActivity::class.java)
                            },
                        ) {
                            Text(
                                text = "FirstAty",
                            )
                        }
                        Button(
                            onClick = {
                                ActivityUtils.startActivity(SecondActivity::class.java)
                            },
                        ) {
                            Text(
                                text = "SecondActivity",
                            )
                        }
                        Button(
                            onClick = {
                                ActivityUtils.startActivity(ThirdActivity::class.java)
                            },
                        ) {
                            Text(text = "ThirdActivity")
                        }
                        Button(
                            onClick = {
                                ActivityUtils.startActivity(CoroutineActivity::class.java)
                            },
                        ) {
                            Text(text = "CoroutineActivity")
                        }
                        Button(
                            onClick = {
                                ActivityUtils.startActivity(ComposeActivity::class.java)
                            },
                        ) {
                            Text(text = "ComposeActivity")
                        }
                    }

                }
            })

        /*
                DisposableEffect(lifecycleOwner) {
                    val observer = LifecycleEventObserver { source, event ->
                        when (event) {
                            Lifecycle.Event.ON_START -> {

                            }

                            Lifecycle.Event.ON_STOP -> {

                            }

                            else -> {}
                        }
                    }

                    val lifecycleOwner = LocalLifecycleOwner.current
                    lifecycleOwner.lifecycle.addObserver(observer)
                    onDispose {
                        lifecycleOwner.lifecycle.removeObserver(observer)
                    }
                }
        */
    }

    @Preview(showBackground = true)
    @Composable
    fun GreetingPreview() {
        Greeting("Android")
    }

}
