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
import com.gmail.jiangyang5157.kotlin_kit.render.Renderable
import com.gmail.jiangyang5157.sudoku.widget.terminal.render.TTerminal
import com.gmail.jiangyang5157.sudoku_presenter.model.Terminal

/**
 * Created by Yang Jiang on July 18, 2017
 */
class TerminalView : RenderView, Renderable<Canvas> {

    companion object {
        const val TAG = "TerminalView"
    }

    private var terminal: TTerminal? = null

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
                // TODO conditional refresh
            }
        }
        return super.onTouchEvent(event)
    }

    override fun onRender(t: Canvas) {
        Log.d(TAG, "onRender")
        t.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR)
        terminal?.onRender(t)
    }

    fun setTerminal(p: Terminal?) {
        this.terminal = p?.let {
            TMapper(width, height).map(p)
        }
        refreshRender()
    }

    fun setCell(index: Int, digit: Int?) {
        this.terminal?.apply {
            C[index].spec.digit = digit ?: 0
        }
        refreshRender()
    }

    fun setPossibility(index: Int, possibility: Array<Int?>) {
        this.terminal?.apply {
            possibility.forEachIndexed { i, p ->
                C[index].P[i].spec.digit = p ?: 0
            }
        }
        refreshRender()
    }

}