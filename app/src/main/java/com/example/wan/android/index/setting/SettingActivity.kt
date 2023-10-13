package com.example.wan.android.index.setting

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.SystemClock
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.lifecycleScope
import com.blankj.utilcode.util.AppUtils
import com.blankj.utilcode.util.ConvertUtils
import com.blankj.utilcode.util.FileUtils
import com.blankj.utilcode.util.PathUtils
import com.blankj.utilcode.util.SPUtils
import com.blankj.utilcode.util.ToastUtils
import com.blankj.utilcode.util.ZipUtils
import com.bumptech.glide.Glide
import com.example.wan.android.R
import com.example.wan.android.base.activity.VVMBaseActivity
import com.example.wan.android.constant.AppConst
import com.example.wan.android.databinding.ActivitySettingBinding
import com.example.wan.android.index.web.WebActivity
import com.example.wan.android.ui.dialog.AppDetailDialog
import com.example.wan.android.utils.UserUtils
import com.example.wan.android.utils.ext.alert
import com.example.wan.android.utils.ext.cancel
import com.example.wan.android.utils.ext.ok
import com.example.wan.android.utils.getUri
import com.example.wan.android.utils.getViewModel
import com.example.wan.android.utils.toast
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import splitties.views.onClick
import java.io.File

class SettingActivity : VVMBaseActivity<SettingViewModel, ActivitySettingBinding>() {

    override val viewModel: SettingViewModel by lazy { getViewModel() }
    override val binding by lazy { ActivitySettingBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
    }

    override fun initStatusBarColor() = R.color.status_bar

    @SuppressLint("SetTextI18n", "IntentReset")
    private fun initView() {
        val cachePath = PathUtils.getExternalAppCachePath()
        val len = FileUtils.getLength(cachePath)
        val size = ConvertUtils.byte2FitMemorySize(len, 2)
        binding.tvCache.text = size
        binding.llCache.onClick {
            alert("提示", "将会清理图片、网站等缓存，是否继续？") {
                cancel {}
                ok {
                    lifecycleScope.launch {
                        withContext(Dispatchers.IO) {
                            Glide.get(activity).clearDiskCache()
                            FileUtils.deleteFilesInDir(cachePath)
                        }
                        toast("清理完成")
                        val lenNew = FileUtils.getLength(cachePath)
                        val sizeNew = ConvertUtils.byte2FitMemorySize(lenNew, 2)
                        binding.tvCache.text = sizeNew
                    }
                }
            }.show()
        }
        initViewLightModel()
        binding.llNightModel.onClick {
            val currentMode = binding.tvNightModel.text
            val nightModeArray = arrayOf("跟随系统", "普通模式", "深色模式")
            val index = try {
                nightModeArray.toList().indexOf(currentMode)
            } catch (e: Exception) {
                0
            }
            var selectModel = SPUtils.getInstance().getInt("night_model")
            alert("深色模式") {
                setSingleChoiceItems(nightModeArray, index) { dialog, which ->
                    selectModel = when (which) {
                        1 -> {
                            AppCompatDelegate.MODE_NIGHT_NO
                        }

                        2 -> {
                            AppCompatDelegate.MODE_NIGHT_YES
                        }

                        else -> {
                            AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
                        }
                    }
                }
                cancel {}
                ok {
                    SPUtils.getInstance().put("night_model", selectModel)
                    initViewLightModel()
                    AppCompatDelegate.setDefaultNightMode(selectModel)
                }
            }.show()
        }
        binding.llWebSite.onClick {
            val url = "https://wanandroid.com/"
            WebActivity.start(url)
        }
        binding.llReport.onClick {
            val email = getString(R.string.email)
            val intent = Intent(Intent.ACTION_SENDTO).apply {
                data = Uri.parse("mailto:")
                putExtra(Intent.EXTRA_EMAIL, arrayOf(email)) // 网易邮箱 未能识别。 Gmail、Mail.ru、Outlook、厂商自带邮件APP 都能正常识别
                putExtra(Intent.EXTRA_SUBJECT, "${getString(R.string.app_name)}-反馈/建议")
//                putExtra(Intent.EXTRA_TEXT, "")
            }
            if (intent.resolveActivity(packageManager) != null) {
                startActivity(Intent.createChooser(intent, "选择邮件APP"))
            } else {
                ToastUtils.showShort("您未安装邮件APP")
            }
        }
        binding.llLog.onClick {
            lifecycleScope.launch(Dispatchers.IO) {
                val zipFilePath =
                    PathUtils.getCachePathExternalFirst() + File.separator + "crash.zip"
                ZipUtils.zipFile(
                    AppConst.crashPath,
                    zipFilePath
                )
                launch(Dispatchers.Main) {
                    val email = getString(R.string.email)
                    // ACTION_SENDTO 无附件
                    // ACTION_SEND 一个附件
                    // ACTION_SEND_MULTIPLE 多个附件
                    /*
                    https://developer.android.com/guide/components/intents-common#Email
                    ACTION_SENDTO (for no attachment) or
                    ACTION_SEND (for one attachment) or
                    ACTION_SEND_MULTIPLE (for multiple attachments)
                    */
                    val intent = Intent(Intent.ACTION_SEND).apply {
                        putExtra(Intent.EXTRA_EMAIL, arrayOf(email)) // 网易邮箱 未能识别。 Gmail、Mail.ru、Outlook、厂商自带邮件APP 都能正常识别
                        putExtra(Intent.EXTRA_SUBJECT, "${getString(R.string.app_name)}-崩溃日志上报")
                        putExtra(Intent.EXTRA_TEXT, com.example.wan.android.utils.AppUtils.getAppInfo(
                            "null",
                            UserUtils.getSupperUserInfo()?.userInfo?.username ?: "null"
                        ))
                        putExtra(Intent.EXTRA_STREAM, File(zipFilePath).getUri())
                        type = "application/octet-stream"
                    }
                    if (intent.resolveActivity(packageManager) != null) {
                        alert("提示", "请选择一款邮件App") {
                            ok {
                                startActivity(Intent.createChooser(intent, "选择邮件APP"))
                            }
                        }.show()
                    } else {
                        ToastUtils.showShort("您未安装邮件APP")
                    }
                }
            }
        }
        binding.llSource.onClick {
            alert("APP 源码", "心急食唔到热豆腐") {
                ok {}
            }.show()
        }
        binding.llVersion.onClick {
            onMultiClick({ i ->
                ToastUtils.showShort("快速再按 $i 次 查看更多")
            }) {
                AppDetailDialog(
                    context = activity,
                    env = "null",
                    uid = "${UserUtils.getSupperUserInfo()?.userInfo?.id ?: "null"}",
                ).show()
            }
        }
        binding.tvVersion.text = "Version ${AppUtils.getAppVersionName()}"
        binding.llLogout.onClick {
            alert("提示", "要注销登录吗?") {
                cancel {}
                ok {
                    viewModel.logout()
                }
            }.show()
        }
    }

    private fun initViewLightModel() {
        binding.tvNightModel.text = when (SPUtils.getInstance().getInt("night_model")) {
            AppCompatDelegate.MODE_NIGHT_NO -> {
                "普通模式"
            }

            AppCompatDelegate.MODE_NIGHT_YES -> {
                "深色模式"
            }

            else -> {
                "跟随系统"
            }
        }
    }

    override fun viewModelObserve() {
        super.viewModelObserve()
        viewModel.logStatus.observe(this) {
            if (it.not()) {
                toast("已注销登录")
                setResult(RESULT_OK)
                finish()
            }
        }
    }

    private val size = 5
    private val interval = 200
    private var mHints: Array<Long?> = arrayOfNulls(size)

    private fun onMultiClick(
        continueListener: ((Int) -> Unit)? = null,
        listener: () -> Unit
    ) {
        System.arraycopy(mHints, 1, mHints, 0, mHints.size - 1) // 每次点击时，数组向前移动一位
        mHints[mHints.size - 1] = SystemClock.uptimeMillis() // 为数组最后一位赋值
        val time = interval * size
        if (SystemClock.uptimeMillis() - (mHints[0] ?: 0L) <= time) { // 连续点击之间有效间隔
            mHints = arrayOfNulls(size)
            listener()
        } else {
            for (i in 0 until size) {
                if (SystemClock.uptimeMillis() - (mHints[i] ?: 0L) <= time) {
                    continueListener?.invoke(i)
                    break
                }
            }
        }
    }

}