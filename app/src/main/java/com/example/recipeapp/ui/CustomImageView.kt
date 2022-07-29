package com.example.recipeapp.ui

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.view.GestureDetectorCompat
import kotlin.math.max
import kotlin.math.min

class CustomImageView: AppCompatImageView {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(
        context: Context,
        attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)


    private var mScaleFactor = 1f

    private val scaleListener = object : ScaleGestureDetector.SimpleOnScaleGestureListener() {

        override fun onScale(detector: ScaleGestureDetector): Boolean {
            mScaleFactor *= detector.scaleFactor

            //小さくならないように1.0-5.0のscaleに収める
            mScaleFactor = max(1.0f, min(mScaleFactor, 5.0f))

            //この２行で描写できるようになってるぽい？
            scaleX = mScaleFactor
            scaleY = mScaleFactor

//            invalidate()
            return true
        }
    }

    //移動
    private val panListener = object : GestureDetector.SimpleOnGestureListener() {

        override fun onScroll(
            e1: MotionEvent?,
            e2: MotionEvent?,
            distanceX: Float,
            distanceY: Float
        ): Boolean {

            translationX -= distanceX
            translationY -= distanceY

//            invalidate()
            return true
        }
    }

    private val mPanGestureDetector = GestureDetectorCompat(context, panListener)


    private val mScaleDetector = ScaleGestureDetector(context, scaleListener)


    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(ev: MotionEvent): Boolean {
        mScaleDetector.onTouchEvent(ev)
        mPanGestureDetector.onTouchEvent(ev)
        return true
    }


    //公式ドキュメントに含まれてたけどonScale内のscale設定だけで描写できてた
    /*
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        canvas?.apply {
            save()
            scale(mScaleFactor, mScaleFactor)
            // onDraw() code goes here
            restore()
        }
    }

     */
}