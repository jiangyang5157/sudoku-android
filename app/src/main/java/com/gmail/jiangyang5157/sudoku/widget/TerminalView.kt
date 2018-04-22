package com.gmail.jiangyang5157.sudoku.widget

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import com.gmail.jiangyang5157.kotlin_android_kit.widget.RenderView

/**
 * Created by Yang Jiang on July 18, 2017
 */
class TerminalView : RenderView, RenderView.OnRenderListener {

    companion object {
        const val TAG = "TerminalView"
    }

    init {
        setZOrderOnTop(true)
        holder.setFormat(PixelFormat.TRANSPARENT)
        setRenderListener(this)
    }

    constructor(context: Context)
            : super(context)

    constructor(context: Context, attrs: AttributeSet)
            : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int)
            : super(context, attrs, defStyleAttr)

    override fun onRender(canvas: Canvas) {
        Log.d(TAG, "onRender")
        canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR)

        // TODO
        val paint = Paint()
        paint.color = Color.BLUE
        paint.textSize = 100F
        canvas.drawText("Hello world.", 200F, 200F, paint)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                renderResume()
            }
            MotionEvent.ACTION_UP -> {
                renderPause()
                renderRefresh()
            }
            MotionEvent.ACTION_MOVE -> {
                // TODO
            }
            MotionEvent.ACTION_CANCEL -> {
                Log.d(TAG, "MotionEvent.ACTION_CANCEL")
                renderPause()
            }
            else -> {
            }
        }
        return super.onTouchEvent(event)
    }

}