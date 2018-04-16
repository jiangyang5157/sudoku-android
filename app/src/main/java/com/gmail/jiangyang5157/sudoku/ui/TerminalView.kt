package com.gmail.jiangyang5157.sudoku.ui

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import com.gmail.jiangyang5157.kotlin_android_view.RenderView

/**
 * Created by Yang Jiang on July 18, 2017
 */
class TerminalView : RenderView {

    constructor(context: Context)
            : super(context)

    constructor(context: Context, attrs: AttributeSet)
            : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int)
            : super(context, attrs, defStyleAttr)

    fun setOnRenderCallback(callback: RenderView.OnRenderListener) {
        setRenderListener(callback)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                renderResume()
            }
            MotionEvent.ACTION_UP -> {
                renderPause()
                renderRefresh()
                performClick()
            }
            MotionEvent.ACTION_MOVE -> {
            }
            MotionEvent.ACTION_CANCEL -> {
                renderPause()
            }
            else -> {
            }
        }
        return super.onTouchEvent(event)
    }

    override fun performClick(): Boolean {
        super.performClick()
        return true
    }

}