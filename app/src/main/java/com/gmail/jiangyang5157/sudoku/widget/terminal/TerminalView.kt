package com.gmail.jiangyang5157.sudoku.widget.terminal

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.PixelFormat
import android.graphics.PorterDuff
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import com.gmail.jiangyang5157.kotlin_android_kit.widget.RenderView
import com.gmail.jiangyang5157.kotlin_kit.math.Vector2i
import com.gmail.jiangyang5157.kotlin_kit.render.Renderable
import com.gmail.jiangyang5157.sudoku.widget.terminal.render.TBoard

/**
 * Created by Yang Jiang on July 18, 2017
 */
class TerminalView : RenderView, Renderable<Canvas> {

    companion object {
        const val TAG = "TerminalView"
    }

    init {
        setZOrderOnTop(true)
        holder.setFormat(PixelFormat.TRANSPARENT)
        setRenderable(this)
    }

    constructor(context: Context)
            : super(context)

    constructor(context: Context, attrs: AttributeSet)
            : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int)
            : super(context, attrs, defStyleAttr)

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                Log.d(TAG, "MotionEvent.ACTION_DOWN")
                refreshRender()
            }
            MotionEvent.ACTION_UP -> {
                Log.d(TAG, "MotionEvent.ACTION_UP")
                refreshRender()
            }
            MotionEvent.ACTION_MOVE -> {
                refreshRender() // TODO conditional refresh
            }
        }
        return super.onTouchEvent(event)
    }

    override fun onRender(t: Canvas) {
        Log.d(TAG, "onRender")
        t.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR)

        // TODO
        TBoard(position = Vector2i(0, 0), w = 500, h = 500).onRender(t)
    }

}