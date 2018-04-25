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
import com.gmail.jiangyang5157.sudoku.widget.terminal.render.TPossibilityTerminal
import com.gmail.jiangyang5157.sudoku_presenter.model.PossibilityTerminal

/**
 * Created by Yang Jiang on July 18, 2017
 */
class TerminalView : RenderView, Renderable<Canvas> {

    companion object {
        const val TAG = "TerminalView"
    }

    private var renderable: TPossibilityTerminal? = null

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
        renderable?.onRender(t)
    }

    fun setTerminal(p: PossibilityTerminal?) {
        this.renderable = p?.let {
            TMapper(width, height).map(p)
        }
    }

    fun setCell(index: Int, digit: Int?) {
        this.renderable?.apply {
            T.C[index].spec.digit = digit
        }
    }

    fun setPossibility(index: Int, possibility: Array<Int?>) {
        this.renderable?.apply {
            possibility.forEachIndexed { i, p ->
                P[index][i].spec.digit = p
            }
        }
    }

}