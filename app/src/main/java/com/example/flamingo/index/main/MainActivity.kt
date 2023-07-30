package com.example.flamingo.index.main

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.blankj.utilcode.util.ActivityUtils
import com.example.flamingo.R
import com.example.flamingo.index.first.FirstAty
import com.example.flamingo.ui.theme.AppTheme
import com.example.utils.getViewModel
import com.example.utils.toJson
import com.example.utils.toast

class MainActivity : AppCompatActivity() {

    private val mainViewModel by lazy {
        getViewModel(MainViewModel::class.java)
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

        mainViewModel.takeUri.observe(this) {
            toast("拍照 >>> \nuri:\n${it.uri}\npath:\n${it.path}")
        }
        mainViewModel.fileUri.observe(this) {
            toast("文件 >>> \nuri:\n${it.uri}\npath:\n${it.path}")
        }
        mainViewModel.pickUri.observe(this) {
            toast("相册 >>> \nuri:\n${it.uri}\npath:\n${it.path}")
        }
        mainViewModel.pickImages.observe(this) {
            toast("相册 Matisse >>> \n${it.toJson()}")
        }
        mainViewModel.uiImage.observe(this) {
            if (it.crop) {
                toast("裁剪 >>> \n${it}\nuri:\n${it.uri}\npath:\n${it.path}")
            }
        }
    }

    @Composable
    fun Greeting(name: String) {
        val current = LocalContext.current

        val viewModel: MainViewModel = viewModel()
        val res by viewModel.uiImage.observeAsState()

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            if (res != null) {
                AsyncImage(
                    model = res?.uri,
                    contentDescription = null,
                    modifier = Modifier
                        .size(80.dp)
                        .clip(CircleShape), // 圆形
                    contentScale = ContentScale.Crop,
                )
            } else {
                Icon(
                    ImageVector.vectorResource(id = R.drawable.ic_launcher_background),
                    contentDescription = null,
                    modifier = Modifier
                        .size(80.dp)
                        .clip(CircleShape),
                )
            }
            Text(
                text = "Hello $name!",
                modifier = Modifier.clickable {
                    ActivityUtils.startActivity(FirstAty::class.java)
                },
                maxLines = 1,
            )
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
                    pickObserver.getContent()
                },
            ) {
                Text(
                    text = "文件",
                    color = Color.Red
                )
            }
            Button(
                onClick = {
                    pickObserver.pickImagesFormMatisse()
                },
            ) {
                Text(
                    text = "图库 Matisse-Compose",
                )
            }
            Divider(modifier = Modifier.height(10.dp))
            Button(
                onClick = {
                    pickObserver.pickImagesFormMatisse(false)
                },
            ) {
                Text(
                    text = "不裁剪 - 图库 Matisse-Compose",
                )
            }
            Button(
                onClick = {
                    pickObserver.getContent(false)
                },
            ) {
                Text(
                    text = "不裁剪 - 文件",
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
        }

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
