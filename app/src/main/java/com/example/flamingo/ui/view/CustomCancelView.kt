package com.example.flamingo.ui.view

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import android.view.animation.DecelerateInterpolator
import com.blankj.utilcode.util.ConvertUtils

class CustomCancelView : View {

    private val STATE_NORMAL = 0x1000 //普通状态

    private val STATE_PROCESSING = 0x2000 //放大状态

    private val OPEN_MASK = 0x0001 //打开状态

    private val CLOSE_MASK = OPEN_MASK shl 1 //关闭状态

    private var mState = STATE_NORMAL

    private var mPaint = Paint(Paint.ANTI_ALIAS_FLAG or Paint.DITHER_FLAG)
    private var mRadius = 0f
    private val mTouchExpand = 20f
    private var isInSide = false
    private var isInit = true
    private val defaultSize = 100

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {

        mPaint.color = Color.RED
        visibility = GONE

        //初始状态隐藏
        resetClose()
    }

    fun resetClose() {
        animate().translationX(ConvertUtils.dp2px(defaultSize.toFloat()).toFloat())
            .translationY(ConvertUtils.dp2px(defaultSize.toFloat()).toFloat())
            .setDuration(0)
            .start()
    }

    fun startAnimate(isOpen: Boolean) {
        startAnimate(isOpen, null)
    }

    fun startAnimate(isOpen: Boolean, callback: (() -> Unit)?) {
        if (isInit) {
            setTranslationX(getMeasuredWidth().toFloat())
            setTranslationY(getMeasuredHeight().toFloat())
            isInit = false
        }
        if (isOpen && mState and OPEN_MASK != 0) {
            return
        }
        if (!isOpen && mState and CLOSE_MASK != 0) {
            return
        }
        animate().translationX((if (isOpen) 0 else getMeasuredWidth()).toFloat())
            .translationY((if (isOpen) 0 else getMeasuredHeight()).toFloat())
            .setDuration(300)
            .setInterpolator(DecelerateInterpolator())
            .setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationStart(animation: Animator) {
                    if (isOpen) {
                        if (getVisibility() != View.VISIBLE) {
                            setVisibility(View.VISIBLE)
                        }
                    }
                    mState = STATE_PROCESSING or if (isOpen) OPEN_MASK else CLOSE_MASK
                }

                override fun onAnimationEnd(animation: Animator) {
                    if (!isOpen) {
                        callback?.invoke()
                    }
                    mState = STATE_NORMAL or if (isOpen) OPEN_MASK else CLOSE_MASK
                }
            })
            .setStartDelay(200)
            .start()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.translate(getWidth().toFloat(), getHeight().toFloat())
        canvas.drawCircle(0f, 0f, mRadius + if (isInSide) mTouchExpand else 0f, mPaint)
    }

    fun isInSide(isInSide: Boolean) {
        this.isInSide = isInSide
        invalidate()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val width: Int
        val height: Int
        var mode = MeasureSpec.getMode(widthMeasureSpec)
        var size = MeasureSpec.getSize(widthMeasureSpec)
        width = if (mode == MeasureSpec.EXACTLY) {
            size
        } else {
            ConvertUtils.dp2px(defaultSize.toFloat())
        }
        mode = MeasureSpec.getMode(heightMeasureSpec)
        size = MeasureSpec.getSize(heightMeasureSpec)
        height = if (mode == MeasureSpec.EXACTLY) {
            size
        } else {
            ConvertUtils.dp2px(defaultSize.toFloat())
        }
        setMeasuredDimension(width, height)
        mRadius = Math.min(width, height) - mTouchExpand
    }

}