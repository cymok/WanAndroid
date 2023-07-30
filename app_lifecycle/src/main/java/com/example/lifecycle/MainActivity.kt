package com.example.lifecycle

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.example.lifecycle.ui.theme.AppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Greeting("Android"){

                    }
                }
            }
        }
        lifecycle.addObserver(MyLifecycleObserver())
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier, onClick: (()->Unit)? = null) {
    val context = LocalContext.current
    Text(
        text = "Hello $name! Next!!",
        modifier = modifier.clickable {
            context.startActivity(Intent(context, SecondActivity::class.java))
//            onClick?.invoke()
        }
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    AppTheme {
        Greeting("Android")
    }
}