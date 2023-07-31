package com.example.flamingo.index.first

import android.Manifest
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import androidx.activity.result.contract.ActivityResultContracts
import androidx.collection.ArrayMap
import androidx.collection.ArraySet
import androidx.collection.arrayMapOf
import androidx.collection.arraySetOf
import androidx.core.content.FileProvider
import androidx.core.view.WindowCompat
import coil.load
import com.example.flamingo.base.BaseActivity
import com.example.flamingo.config.matiss.CoilImageEngine
import com.example.flamingo.databinding.AtyMainBinding
import com.example.flamingo.index.second.SecondActivity
import com.example.flamingo.index.second.SecondActivityResultContract
import com.example.flamingo.utils.UCropUtil
import com.example.flamingo.utils.getFile
import com.example.flamingo.utils.loadAvatar
import com.example.flamingo.utils.reqPermissions
import com.example.flamingo.utils.toast
import github.leavesczy.matisse.Matisse
import github.leavesczy.matisse.MatisseContract
import github.leavesczy.matisse.MediaResource
import github.leavesczy.matisse.MimeType
import github.leavesczy.matisse.SmartCaptureStrategy
import io.reactivex.internal.util.LinkedArrayList
import java.io.File
import java.util.LinkedList
import java.util.TreeMap
import java.util.TreeSet

class FirstActivity : BaseActivity() {

    private val binding by lazy {
        AtyMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        // registerForActivityResult 必须再 onStart 之前执行，可在 onCreate 或 作为 成员变量，建议放到 LifecycleObserver 处理

        // 自定义的 Contract
        val secondActivityLauncher = registerForActivityResult(SecondActivityResultContract()) {
            toast(it)
        }
        binding.bt0.setOnClickListener {
            // 自定义 Contract
            secondActivityLauncher.launch("qwer")
        }


        // 通用 Contract 获取Activity返回结果 指定Intent
        val atyContract =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { atyResult ->
                atyResult.data?.takeIf { atyResult.resultCode == RESULT_OK }
                    ?.getStringExtra("result")?.let { result ->
                        toast(result)
                    }
            }
        binding.bt00.setOnClickListener {
            // 通用 Contract
            atyContract.launch(
                Intent(this, SecondActivity::class.java)
                    .apply {
                        putExtra("data", ">>>")
                    })
        }


        // 拍照
        val takeContract =
            registerForActivityResult(ActivityResultContracts.TakePicture()) {
                if (it) {
                    val uri = takePictureUri
                    cropImg(uri)
                } else {
                    toast("已取消拍照")
                }
            }
        binding.bt1.setOnClickListener {
            takeContract.launch(takePictureUri)
        }


        // 从相册选择图片
        val contract =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { atyResult ->
                if (atyResult.resultCode == RESULT_OK) {
                    val uri = atyResult.data?.data
                    if (uri != null) {
                        val path = uri.getFile()?.absolutePath
                        binding.tvUri.text = uri.toString()
                        binding.tvPath.text = path
                        binding.iv.load(uri)
                        cropImg(uri)
                    } else {
                        toast("uri 无效")
                    }
                }
            }
        binding.bt2.setOnClickListener {
            contract.launch(Intent(Intent.ACTION_PICK).apply {
                setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*")
            })
        }


        // Intent.ACTION_GET_CONTENT 拿到的 uri 不能拿到对应文件的路径
        // 选择文件 指定content-type
        val fileContract =
            registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
                if (uri != null) {
                    val path = uri.getFile()?.absolutePath
                    binding.tvUri.text = uri.toString()
                    binding.tvPath.text = path
                    binding.iv.load(uri)
                    cropImg(uri)
                } else {
                    toast("uri 无效")
                }
            }
        // WHAT
        binding.bt3.setOnClickListener {
            fileContract.launch("image/*")
        }
        binding.bt4.setOnClickListener {

            // List
            ArrayList<Any>() // 引用类型是 ArrayList 值类型是 java的ArrayList
            //
            LinkedList<Any>() // java的LinkedList
            LinkedArrayList(9) // rxjava2 自己实现的类 不是实现List接口
            //
            listOf<String>() // List = EmptyList:List
            mutableListOf<Any>() // MutableList = ArrayList
            //
            arrayListOf<Any>() // ArrayList

            // Set
            HashSet<Any>() // 引用类型是 HashSet 值类型是 java的HashSet
            LinkedHashSet<Any>() // 引用类型是 LinkedHashSet 值类型是 java的LinkedHashSet
            //
            TreeSet<Any>() // java的TreeSet
            ArraySet<Any>() // androidx.collection.ArraySet
            //
            setOf<Any>() // Set = EmptySet:Set
            mutableSetOf<Any>() // MutableSet = LinkedHashSet
            //
            hashSetOf<Any>() // HashSet
            linkedSetOf<Any>() // LinkedHashSet
            sortedSetOf<Any>() // TreeSet
            arraySetOf<Any>() // ArraySet

            // Map
            HashMap<String, Any>() // 引用类型是HashMap 值类型是java的HashMap
            LinkedHashMap<String, Any>() // 引用类型是LinkedHashMap 值类型是java的LinkedHashMap
            //
            TreeMap<String, Any>() // java的TreeMap
            ArrayMap<String, Any>() // androidx.collection.ArrayMap
            //
            mapOf<String, Any>() // Map = EmptyMap:Map
            mutableMapOf<String, Any>() // MutableMap = LinkedHashMap
            //
            hashMapOf<String, Any>() // HashMap
            linkedMapOf<String, Any>() // LinkedHashMap
            sortedMapOf<String, Any>() // SortedMap = TreeMap
            arrayMapOf<String, Any>() // ArrayMap

        }

        binding.bt5.setOnClickListener {
            val permissions = if (Build.VERSION.SDK_INT >= 33) {
                Manifest.permission.READ_MEDIA_IMAGES
            } else {
                Manifest.permission.READ_EXTERNAL_STORAGE
            }
            reqPermissions(listOf(permissions)) {

            }
        }

        binding.bt6.setOnClickListener {
            val action = {

            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                reqPermissions(listOf(Manifest.permission.REQUEST_INSTALL_PACKAGES)) { action.invoke() }
            } else {
                action.invoke()
            }
        }

        binding.bt7.setOnClickListener {
            pickImg()
        }

        val mediaPickerLauncher =
            registerForActivityResult(MatisseContract()) { result: List<MediaResource> ->
                if (result.isNotEmpty()) {
                    val mediaResource = result[0]
                    val uri = mediaResource.uri
                    val path = mediaResource.path
                    val name = mediaResource.name
                    val mimeType = mediaResource.mimeType
                    toast(mediaResource.toString())
                }
            }
        binding.bt8.setOnClickListener {
            val matisse = Matisse(
                maxSelectable = 1,
                mimeTypes = MimeType.ofImage(hasGif = true),
                imageEngine = CoilImageEngine(),
                captureStrategy = SmartCaptureStrategy("$packageName.fileProvider")
            )
            mediaPickerLauncher.launch(matisse)
        }

    }

    private fun pickImg() {

    }

    private val takePictureUri by lazy {
        val file = File(externalCacheDir, "take_picture.jpeg")
        if (Build.VERSION.SDK_INT >= 24) {
            FileProvider.getUriForFile(this, "$packageName.fileProvider", file)
        } else {
            Uri.fromFile(file)
        }
    }

    private val cropPictureUri by lazy {
        val file = File(externalCacheDir, "crop_picture.jpeg")
        Uri.fromFile(file)
    }

    private fun cropImg(sourceUri: Uri) {
        UCropUtil.crop(this, sourceUri, cropPictureUri) { resultUri ->
            if (resultUri != null) {

//            val bitmap = resultUri?.let {
//                // 直接用 setImageURI 同一命名的拍照图片可能会因媒体库没刷新导致不显示最新图片 所以转用 bitmap
//                BitmapFactory.decodeStream(contentResolver.openInputStream(it))
//            }
                val descriptor = contentResolver.openFileDescriptor(resultUri, "r")
                val bitmap = BitmapFactory.decodeFileDescriptor(descriptor?.fileDescriptor)
                descriptor?.close()
                binding.iv.loadAvatar(bitmap)
                val path = resultUri.getFile()?.absolutePath
                binding.tvUri.text = resultUri.toString()
                binding.tvPath.text = path
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        UCropUtil.onActivityResult(requestCode, resultCode, data)
    }

}