package com.example.flamingo

import android.app.Application
import android.content.Context
import android.os.Build
import coil.Coil
import coil.ImageLoader
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import coil.decode.VideoFrameDecoder

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        initCoil(this)
    }

    private fun initCoil(context: Context) {
        val imageLoader = ImageLoader.Builder(context = context)
            .components {
                //可选，需要展示 Gif 则引入
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    add(ImageDecoderDecoder.Factory())
                } else {
                    add(GifDecoder.Factory())
                }
                //可选，需要展示 Video 则引入
                add(VideoFrameDecoder.Factory())
            }
            .build()
        Coil.setImageLoader(imageLoader)
    }

}