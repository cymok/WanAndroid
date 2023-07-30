package com.example.flamingo.index.main

import android.Manifest
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.example.flamingo.CoilImageEngine
import com.example.flamingo.data.PickData
import com.example.flamingo.utils.getFile
import com.example.flamingo.utils.getUri
import com.example.utils.getViewModel
import com.example.utils.reqPermissions
import com.yalantis.ucrop.UCrop
import github.leavesczy.matisse.Matisse
import github.leavesczy.matisse.MatisseContract
import github.leavesczy.matisse.MediaResource
import github.leavesczy.matisse.MimeType
import github.leavesczy.matisse.SmartCaptureStrategy
import java.io.File
import kotlin.math.absoluteValue
import kotlin.random.Random

class PickObserver : DefaultLifecycleObserver {

    private var context: Context
    private var viewModel: MainViewModel

    // 保存类型 FragmentActivity or Fragment
    private var owner: LifecycleOwner

    private var crop = true

    private lateinit var takePictureUri: Uri
    private lateinit var cropPictureUri: Uri

    private lateinit var cropLauncher: ActivityResultLauncher<Intent>
    private lateinit var takeLauncher: ActivityResultLauncher<Uri>
    private lateinit var fileLauncher: ActivityResultLauncher<String>
    private lateinit var pickLauncher: ActivityResultLauncher<Intent>
    private lateinit var mediaPickerLauncher: ActivityResultLauncher<Matisse>

    constructor(activity: FragmentActivity) {
        context = activity
        owner = activity
        viewModel = activity.getViewModel(MainViewModel::class.java)
    }

    constructor(fragment: Fragment) {
        context = fragment.requireContext()
        owner = fragment
        viewModel = fragment.getViewModel(MainViewModel::class.java)
    }

    override fun onCreate(owner: LifecycleOwner) {

        // 裁剪
        cropLauncher =
            (if (owner is FragmentActivity) (owner as FragmentActivity) else (owner as Fragment))
                .registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { atyResult ->
                    if (atyResult.resultCode == AppCompatActivity.RESULT_OK) {
                        val uri = atyResult.data?.data ?: cropPictureUri
                        onResult(uri)
                    } else {
                        // 取消裁剪
                    }
                }

        // 拍照
        takeLauncher =
            (if (owner is FragmentActivity) (owner as FragmentActivity) else (owner as Fragment))
                .registerForActivityResult(ActivityResultContracts.TakePicture()) { result ->
                    if (result) {
                        val uri = takePictureUri
                        viewModel.takeUri.postValue(PickData(crop, uri, uri.getFile().absolutePath))
                        if (crop) {
                            crop(uri)
                        } else {
                            onResult(uri)
                        }
                    } else {
                        // 取消拍照
                    }
                }

        // 选择文件 指定content-type
        fileLauncher =
            (if (owner is FragmentActivity) (owner as FragmentActivity) else (owner as Fragment))
                .registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
                    if (uri != null) {
                        viewModel.fileUri.postValue(PickData(crop, uri, uri.getFile().absolutePath))
                        if (crop) {
                            crop(uri)
                        } else {
                            onResult(uri)
                        }
                    } else {
                        // 取消选择 或 文件无效
                    }
                }

        // 从相册选择图片
        pickLauncher =
            (if (owner is FragmentActivity) (owner as FragmentActivity) else (owner as Fragment))
                .registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { atyResult ->
                    if (atyResult.resultCode == AppCompatActivity.RESULT_OK) {
                        val uri = atyResult.data?.data
                        if (uri != null) {
                            viewModel.pickUri.postValue(
                                PickData(
                                    crop,
                                    uri,
                                    uri.getFile().absolutePath
                                )
                            )
                            if (crop) {
                                crop(uri)
                            } else {
                                onResult(uri)
                            }
                        } else {
                            // 文件无效
                        }
                    } else {
                        // 取消选择
                    }
                }

        // Matisse-Compose
        mediaPickerLauncher =
            (if (owner is FragmentActivity) (owner as FragmentActivity) else (owner as Fragment))
                .registerForActivityResult(MatisseContract()) { result: List<MediaResource> ->
                    if (result.isNotEmpty()) {
                        viewModel.pickImages.postValue(result)
                        val uri = result[0].uri
                        if (crop) {
                            crop(uri)
                        } else {
                            onResult(uri)
                        }
                    }
                }

    }

    private fun onResult(uri: Uri) {
        val file = uri.getFile()
        val data = PickData(crop, uri, file.absolutePath)
        viewModel.uiImage.postValue(data)
    }

    private fun crop(inUri: Uri, outUri: Uri? = null) {

        cropPictureUri = outUri
            ?: let {
                val file = File(context.externalCacheDir, "crop_${inUri.getFile().name}")
                file.getUri()
            }

        val cropIntent = UCrop.of(inUri, cropPictureUri)
            .withAspectRatio(1f, 1f)
            .withMaxResultSize(512, 512)
            .withOptions(UCrop.Options().apply {
                setStatusBarColor(0)
                setCompressionFormat(Bitmap.CompressFormat.JPEG)
                setCompressionQuality(100)
                setCircleDimmedLayer(true)
            })
            .getIntent(context)
        cropLauncher.launch(cropIntent)
    }

    /**
     * 调用 相机 拍照
     */
    fun takePicture(crop: Boolean = true) {
        this.crop = crop

        val file = File(context.externalCacheDir, "take_${Random.nextLong().absoluteValue}.jpeg")
        takePictureUri = file.getUri()

        takeLauncher.launch(takePictureUri)
    }

    /**
     * 从 FileContent 选择
     */
    @Deprecated("MediaStore 不能实时更新内容 会有无效文件 体验不好")
    fun getContent(crop: Boolean = true) {
        this.crop = crop
        fileLauncher.launch("image/*")
    }

    /**
     * 从 相册 选择
     */
    fun pickAlbum(crop: Boolean = true) {
        this.crop = crop
        pickLauncher.launch(Intent(Intent.ACTION_PICK).apply {
            setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*")
        })
    }

    /**
     * 从 Matisse 选择
     */
    fun pickImagesFormMatisse(crop: Boolean = true) {
        this.crop = crop
        val mimeTypes = MimeType.ofImage(hasGif = false)
        val permissionList = if (Build.VERSION.SDK_INT >= 33) {
            when (mimeTypes) {
                MimeType.ofImage() -> {
                    listOf(Manifest.permission.READ_MEDIA_IMAGES)
                }

                MimeType.ofVideo() -> {
                    listOf(Manifest.permission.READ_MEDIA_VIDEO)
                }

                else -> {
                    listOf(
                        Manifest.permission.READ_MEDIA_IMAGES,
                        Manifest.permission.READ_MEDIA_VIDEO
                    )
                }
            }
        } else {
            listOf(Manifest.permission.READ_EXTERNAL_STORAGE)
        }

        val action = {
            val matisse = Matisse(
                maxSelectable = 1,
                mimeTypes = mimeTypes,
                imageEngine = CoilImageEngine(),
                captureStrategy = SmartCaptureStrategy("${context.packageName}.fileProvider")
            )
            mediaPickerLauncher.launch(matisse)
        }
        if (owner is FragmentActivity) {
            (owner as FragmentActivity).reqPermissions(permissionList, action)
        } else {
            (owner as Fragment).reqPermissions(permissionList, action)
        }
    }

}
