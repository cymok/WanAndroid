package com.example.flamingo

import android.os.Parcel
import android.os.Parcelable
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.layout.ContentScale
import coil.compose.AsyncImage
import github.leavesczy.matisse.ImageEngine
import github.leavesczy.matisse.MediaResource

class CoilImageEngine() : ImageEngine {

    constructor(parcel: Parcel) : this() {
    }

    @Composable
    override fun Image(
        modifier: Modifier,
        mediaResource: MediaResource,
        contentScale: ContentScale
    ) {
        AsyncImage(
            modifier = modifier,
            model = mediaResource.uri,
            contentDescription = mediaResource.name,
            contentScale = contentScale,
            filterQuality = FilterQuality.None
        )
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
//        super.writeToParcel(parcel, flags)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<CoilImageEngine> {
        override fun createFromParcel(parcel: Parcel): CoilImageEngine {
            return CoilImageEngine(parcel)
        }

        override fun newArray(size: Int): Array<CoilImageEngine?> {
            return arrayOfNulls(size)
        }
    }

}