package com.example.flamingo.others.main

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresPermission
import androidx.fragment.app.Fragment
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.example.flamingo.config.matiss.CoilImageEngine
import com.example.flamingo.data.ImageInfo
import com.example.flamingo.utils.getFile
import com.example.flamingo.utils.getUri
import com.example.flamingo.utils.getViewModel
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
    private var viewModel: PickViewModel

    private var crop = true

    private lateinit var takePictureUri: Uri
    private lateinit var cropPictureUri: Uri

    private lateinit var cropLauncher: ActivityResultLauncher<Intent>
    private lateinit var takeLauncher: ActivityResultLauncher<Uri>
    private lateinit var fileLauncher: ActivityResultLauncher<String>
    private lateinit var albumLauncher: ActivityResultLauncher<Intent>
    private lateinit var matisseLauncher: ActivityResultLauncher<Matisse>

    constructor(activity: ComponentActivity) {
        context = activity
        viewModel = activity.getViewModel()
    }

    constructor(fragment: Fragment) {
        context = fragment.requireContext()
        viewModel = fragment.getViewModel()
    }

    override fun onCreate(owner: LifecycleOwner) {

        // 裁剪
        cropLauncher =
            (if (owner is ComponentActivity) (owner as ComponentActivity) else (owner as Fragment))
                .registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { atyResult ->
                    if (atyResult.resultCode == Activity.RESULT_OK) {
                        val uri = atyResult.data?.data ?: cropPictureUri
                        onEndResult(uri)
                    } else {
                        // 取消裁剪
                        viewModel.errCode.postValue(PickViewModel.ERR_CANCEL)
                    }
                }

        // 拍照
        takeLauncher =
            (if (owner is ComponentActivity) (owner as ComponentActivity) else (owner as Fragment))
                .registerForActivityResult(ActivityResultContracts.TakePicture()) { result ->
                    if (result) {
                        val uri = takePictureUri
                        val file = uri.getFile()
                        if (file != null) {
                            if (crop) {
                                crop(uri)
                            } else {
                                onEndResult(uri)
                            }
                        } else {
                            // 文件无效
                            viewModel.errCode.postValue(PickViewModel.ERR_INVALID)
                        }
                    } else {
                        // 取消拍照
                        viewModel.errCode.postValue(PickViewModel.ERR_CANCEL)
                    }
                }

        // 选择文件 指定content-type
        fileLauncher =
            (if (owner is ComponentActivity) (owner as ComponentActivity) else (owner as Fragment))
                .registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
                    if (uri != null) {
                        val file = uri.getFile()
                        if (file != null) {
                            if (crop) {
                                crop(uri)
                            } else {
                                onEndResult(uri)
                            }
                        } else {
                            // 文件无效
                            viewModel.errCode.postValue(PickViewModel.ERR_INVALID)
                        }
                    } else {
                        // 取消选择
                        viewModel.errCode.postValue(PickViewModel.ERR_CANCEL)
                    }
                }

        // 从相册选择图片
        albumLauncher =
            (if (owner is ComponentActivity) (owner as ComponentActivity) else (owner as Fragment))
                .registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { atyResult ->
                    if (atyResult.resultCode == Activity.RESULT_OK) {
                        val uri = atyResult.data?.data
                        if (uri != null) {
                            val file = uri.getFile()
                            if (file != null) {
                                if (crop) {
                                    crop(uri)
                                } else {
                                    onEndResult(uri)
                                }
                            } else {
                                // 文件无效
                                viewModel.errCode.postValue(PickViewModel.ERR_INVALID)
                            }
                        } else {
                            // 文件无效
                            viewModel.errCode.postValue(PickViewModel.ERR_INVALID)
                        }
                    } else {
                        // 取消选择
                        viewModel.errCode.postValue(PickViewModel.ERR_CANCEL)
                    }
                }

        // Matisse-Compose
        matisseLauncher =
            (if (owner is ComponentActivity) (owner as ComponentActivity) else (owner as Fragment))
                .registerForActivityResult(MatisseContract()) { result: List<MediaResource> ->
                    if (result.isNotEmpty()) {
                        val uri = result[0].uri
                        if (crop) {
                            crop(uri)
                        } else {
                            onEndResult(uri)
                        }
                    } else {
                        viewModel.errCode.postValue(PickViewModel.ERR_CANCEL)
                    }
                }

    }

    private fun onEndResult(uri: Uri) {
        val data = ImageInfo(crop, uri, uri.getFile()?.absolutePath ?: "")
        viewModel.imageInfo.postValue(data)
    }

    private fun crop(inUri: Uri, outUri: Uri? = null) {

        cropPictureUri = outUri
            ?: let {
                val fileName = "crop_${inUri.getFile()?.name ?: "${Random.nextLong()}"}"
                val file = File(context.externalCacheDir, fileName)
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
    @RequiresPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
    fun pickFile(crop: Boolean = true) {
        this.crop = crop
        fileLauncher.launch("image/*")
    }

    /**
     * 从 相册 选择
     */
    fun pickAlbum(crop: Boolean = true) {
        this.crop = crop
        albumLauncher.launch(Intent(Intent.ACTION_PICK).apply {
            setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*")
        })
    }

    /**
     * 从 Matisse 选择
     */
    @RequiresPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
    fun pickMatisse(crop: Boolean = true) {
        this.crop = crop
        val matisse = Matisse(
            maxSelectable = 1,
            mimeTypes = MimeType.ofImage(hasGif = false),
            imageEngine = CoilImageEngine(),
            captureStrategy = SmartCaptureStrategy("${context.packageName}.pick.fileProvider")
        )
        matisseLauncher.launch(matisse)
    }

}
