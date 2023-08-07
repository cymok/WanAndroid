package com.example.flamingo.ui.view

import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.graphics.PixelFormat
import android.graphics.Rect
import android.os.Build
import android.util.Log
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import android.view.ViewConfiguration
import android.view.WindowManager
import android.view.animation.BounceInterpolator
import android.widget.ImageView
import androidx.annotation.DrawableRes
import com.blankj.utilcode.util.ScreenUtils
import com.example.flamingo.R
import com.example.flamingo.utils.dp2px

class FloatViewHelper111 {

    companion object {
        private const val mViewWidth = 50
        private const val mCancelViewSize = 100
    }

    private var mWindowManager: WindowManager? = null
    private var mFloatViewLayoutParams: WindowManager.LayoutParams? = null
    private var mFloatView: ImageView? = null
    private var mCustomCancelView: CustomCancelView? = null
    private val mDeleteRect = Rect()
    private var statusBarHeight = 0
    private var res = 0
    private var listener: View.OnClickListener? = null

    fun init(
        context: Activity,
        @DrawableRes res: Int = R.mipmap.ic_launcher,
        listener: View.OnClickListener? = null
    ) {
        this.res = res
        this.listener = listener
        showWindow(context)
    }

    @SuppressLint("CheckResult")
    private fun showWindow(context: Activity) {
        if (null == mWindowManager) {
            mWindowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        }
        if (null == mFloatView) {
            mFloatView = ImageView(context)
//            val lp = RelativeLayout.LayoutParams(20, 20)
//            lp.width = (mViewWidth - 20).dp2px
//            lp.height = (mViewWidth - 20).dp2px
//            mFloatView!!.layoutParams = lp
            mFloatView!!.setImageResource(if (res != 0) res else android.R.mipmap.sym_def_app_icon)

            mFloatViewLayoutParams = WindowManager.LayoutParams()

            // 需要权限的type
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                mFloatViewLayoutParams!!.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
            } else {
                mFloatViewLayoutParams!!.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT
            }
            mFloatViewLayoutParams!!.format = PixelFormat.RGBA_8888 //窗口透明
            mFloatViewLayoutParams!!.gravity = Gravity.START or Gravity.TOP //窗口位置
            mFloatViewLayoutParams!!.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL or
                    WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE

            mFloatViewLayoutParams!!.width = mViewWidth.dp2px
            mFloatViewLayoutParams!!.height = mViewWidth.dp2px
            // 可以修改View的初始位置
            mFloatViewLayoutParams!!.x =
                ScreenUtils.getScreenWidth() - mViewWidth
            mFloatViewLayoutParams!!.y =
                ScreenUtils.getScreenHeight() / 2 - mViewWidth / 2

            // 无需权限的type
            mFloatViewLayoutParams!!.type =
                WindowManager.LayoutParams.TYPE_APPLICATION_ATTACHED_DIALOG
            mFloatViewLayoutParams!!.token = context.window.decorView.windowToken
            mWindowManager!!.addView(mFloatView, mFloatViewLayoutParams)
        }
        if (null == mCustomCancelView) {
            mCustomCancelView = CustomCancelView(context)
            val cancelViewLayoutParams = WindowManager.LayoutParams()
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                cancelViewLayoutParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
            } else {
                cancelViewLayoutParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT
            }
            cancelViewLayoutParams.format = PixelFormat.RGBA_8888
            cancelViewLayoutParams.gravity = Gravity.END or Gravity.BOTTOM
            cancelViewLayoutParams.flags =
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE or WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
            cancelViewLayoutParams.width = mCancelViewSize.dp2px
            cancelViewLayoutParams.height = mCancelViewSize.dp2px

            cancelViewLayoutParams.type =
                WindowManager.LayoutParams.TYPE_APPLICATION_ATTACHED_DIALOG
            cancelViewLayoutParams.token = context.window.decorView.windowToken
            mWindowManager!!.addView(mCustomCancelView, cancelViewLayoutParams)
        }
        initListener(context)
    }

    fun dismissWindow() {
        if (mWindowManager != null && mFloatView != null) {
            mWindowManager!!.removeViewImmediate(mFloatView)
            mCustomCancelView!!.startAnimate(false) {
                mWindowManager!!.removeViewImmediate(mCustomCancelView)
                mWindowManager = null
                mCustomCancelView = null
                mFloatView = null
            }
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun initListener(context: Context) {
        mFloatView!!.setOnClickListener(listener)
        val mTouchSlop = ViewConfiguration.get(context).scaledTouchSlop
        val resourceId = context.resources.getIdentifier("status_bar_height", "dimen", "android")
        if (resourceId > 0) {
            statusBarHeight = context.resources.getDimensionPixelSize(resourceId)
        }
        //设置触摸滑动事件
        mFloatView!!.setOnTouchListener(object : OnTouchListener {
            var startX = 0
            var startY //起始点
                    = 0
            var isPerformClick //是否点击
                    = false
            var finalMoveX //最后通过动画将mView的X轴坐标移动到finalMoveX
                    = 0
            var isRemove = false

            @SuppressLint("ClickableViewAccessibility")
            override fun onTouch(v: View, event: MotionEvent): Boolean {
                if (mDeleteRect.isEmpty) {
                    mDeleteRect.apply {
                        left = ScreenUtils.getScreenWidth() - mCustomCancelView!!.measuredWidth
                        top = ScreenUtils.getScreenHeight() - mCustomCancelView!!.measuredHeight
                        right = ScreenUtils.getScreenWidth()
                        bottom = ScreenUtils.getScreenHeight()
                    }
//                    mDeleteRect.left += mFloatView!!.width / 2
//                    mDeleteRect.top += mFloatView!!.height / 2
                }
                Log.d("click", "onTouch: " + event.action)
                when (event.action) {
                    MotionEvent.ACTION_DOWN -> {
                        startX = event.x.toInt()
                        startY = event.y.toInt()
                        isPerformClick = true
                        return true
                    }

                    MotionEvent.ACTION_MOVE -> {
                        //判断是CLICK还是MOVE
                        //只要移动过，就认为不是点击
                        if (Math.abs(startX - event.x) >= mTouchSlop
                            || Math.abs(startY - event.y) >= mTouchSlop
                        ) {
                            isPerformClick = false
                        }
                        mFloatViewLayoutParams!!.x = (event.rawX - startX).toInt()
                        //这里修复了刚开始移动的时候，悬浮窗的y坐标是不正确的，要减去状态栏的高度，可以将这个去掉运行体验一下
                        mFloatViewLayoutParams!!.y = (event.rawY - startY - statusBarHeight).toInt()
                        Log.e("TAG", "x---->" + mFloatViewLayoutParams!!.x)
                        Log.e("TAG", "y---->" + mFloatViewLayoutParams!!.y)
                        updateViewLayout() //更新mView 的位置
                        mCustomCancelView!!.startAnimate(true)
                        val remove = isRemove(
                            mFloatViewLayoutParams!!.x + (mViewWidth / 2),
                            mFloatViewLayoutParams!!.y + (mViewWidth / 2)
                        )
                        mCustomCancelView!!.isInSide(remove)
                        return true
                    }

                    MotionEvent.ACTION_UP -> {
                        if (isPerformClick) {
                            mFloatView!!.performClick()
                        }
                        isRemove = isRemove(
                            mFloatViewLayoutParams!!.x + (mViewWidth / 2),
                            mFloatViewLayoutParams!!.y + (mViewWidth / 2)
                        )

                        //判断mView是在Window中的位置，以中间为界
                        finalMoveX = if (mFloatViewLayoutParams!!.x + mFloatView!!.measuredWidth / 2
                            >= ScreenUtils.getScreenWidth() / 2
                        ) {
                            ScreenUtils.getScreenWidth() - mFloatView!!.measuredWidth
                        } else {
                            0
                        }
                        if (isRemove) {
                            onClose()
                            dismissWindow()
                        } else {
                            mCustomCancelView!!.startAnimate(false)
                            stickToSide()
                        }
                        if (isPerformClick) {
                            mCustomCancelView!!.resetClose()
                        }
                        return !isPerformClick
                    }

                    else -> {}
                }
                return false
            }

            private fun stickToSide() {
                val animator = ValueAnimator.ofInt(mFloatViewLayoutParams!!.x, finalMoveX)
                    .setDuration(Math.abs(mFloatViewLayoutParams!!.x - finalMoveX).toLong())
                animator.interpolator = BounceInterpolator()
                animator.addUpdateListener { animation: ValueAnimator ->
                    mFloatViewLayoutParams!!.x = animation.animatedValue as Int
                    updateViewLayout()
                }
                animator.start()
            }
        })
    }

    private fun onClose() {
        // TODO: 2019/4/29
    }

    private fun isRemove(centerX: Int, centrY: Int): Boolean {
        //todo 横竖屏切换
        return mDeleteRect.contains(centerX, centrY)
    }

    private fun updateViewLayout() {
        if (null != mFloatView && null != mFloatViewLayoutParams) {
            mWindowManager!!.updateViewLayout(mFloatView, mFloatViewLayoutParams)
        }
    }

    fun hideWindow() {
        if (mFloatView != null) {
            mFloatView!!.visibility = View.GONE
        }
        if (mCustomCancelView != null) {
            mCustomCancelView!!.visibility = View.GONE
        }
    }

    fun visibleWindow() {
        if (mFloatView != null) {
            mFloatView!!.visibility = View.VISIBLE
        }
        if (mCustomCancelView != null) {
            mCustomCancelView!!.visibility = View.VISIBLE
        }
    }

    fun isAvailable() = mFloatView?.isAttachedToWindow ?: false

}