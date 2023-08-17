package com.example.wan.android.index.setting

import android.os.Bundle
import android.os.SystemClock
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.lifecycleScope
import com.blankj.utilcode.util.ConvertUtils
import com.blankj.utilcode.util.FileUtils
import com.blankj.utilcode.util.PathUtils
import com.blankj.utilcode.util.SPUtils
import com.bumptech.glide.Glide
import com.example.wan.android.AppDetailDialog
import com.example.wan.android.R
import com.example.wan.android.base.activity.VVMBaseActivity
import com.example.wan.android.databinding.ActivitySettingBinding
import com.example.wan.android.index.web.WebActivity
import com.example.wan.android.utils.UserUtils
import com.example.wan.android.utils.getViewModel
import com.example.wan.android.utils.toast
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import splitties.views.onClick

class SettingActivity : VVMBaseActivity<SettingViewModel, ActivitySettingBinding>() {

    override val viewModel: SettingViewModel by lazy { getViewModel() }
    override val binding by lazy { ActivitySettingBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
    }

    override fun initStatusBarColor() = R.color.status_bar

    private fun initView() {
        val cachePath = PathUtils.getExternalAppCachePath()
        val len = FileUtils.getLength(cachePath)
        val size = ConvertUtils.byte2FitMemorySize(len, 2)
        binding.tvCache.text = size
        binding.llCache.onClick {
            AlertDialog.Builder(this)
                .setTitle("提示")
                .setMessage("将会清理图片、网站等缓存，是否继续？")
                .setNegativeButton("取消", null)
                .setPositiveButton("确定") { dialog, which ->
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
                .show()
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
            var selectModel = SPUtils.getInstance().getInt("night_module")
            AlertDialog.Builder(this)
                .setTitle("选择模式")
                .setSingleChoiceItems(nightModeArray, index) { dialog, which ->
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
                .setNegativeButton("取消", null)
                .setPositiveButton("确定") { dialog, which ->
                    SPUtils.getInstance().put("night_module", selectModel)
                    initViewLightModel()
                    AppCompatDelegate.setDefaultNightMode(selectModel)
                }.show()
        }
        binding.llWebSite.onClick {
            val url = "https://wanandroid.com/"
            WebActivity.start(url)
        }
        binding.llSource.onClick {}
        binding.llVersion.onClick {
            onMultiClick {
                AppDetailDialog(
                    context = activity,
                    env = "null",
                    uid = UserUtils.getSupperUserInfo()?.userInfo?.username ?: "null",
                ).show()
            }
        }
        binding.llLogout.onClick {
            AlertDialog.Builder(this)
                .setTitle("提示")
                .setMessage("要注销登录吗?")
                .setNegativeButton("取消", null)
                .setPositiveButton("确定") { dialog, which ->
                    lifecycleScope.launch {
                        viewModel.logout()
                    }
                }
                .show()
        }
    }

    private fun initViewLightModel() {
        binding.tvNightModel.text = when (SPUtils.getInstance().getInt("night_module")) {
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
    private val time = 200 * size
    private var mHints: Array<Long?> = arrayOfNulls(size)

    private fun onMultiClick(listener: () -> Unit) {
        System.arraycopy(mHints, 1, mHints, 0, mHints.size - 1)//每次点击时，数组向前移动一位
        mHints[mHints.size - 1] = SystemClock.uptimeMillis()//为数组最后一位赋值
        if (SystemClock.uptimeMillis() - (mHints[0] ?: 0L) <= time) {//连续点击之间有效间隔
            mHints = arrayOfNulls(size)
            listener()
        }
    }

}