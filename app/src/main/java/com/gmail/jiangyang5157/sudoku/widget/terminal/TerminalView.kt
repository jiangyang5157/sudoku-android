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
import com.gmail.jiangyang5157.sudoku.widget.terminal.render.TCell
import com.gmail.jiangyang5157.sudoku.widget.terminal.render.TTerminal
import com.gmail.jiangyang5157.sudoku.widget.terminal.render.spec.TCellFocusd
import com.gmail.jiangyang5157.sudoku.widget.terminal.render.spec.TCellHighlightd
import com.gmail.jiangyang5157.sudoku.widget.terminal.render.spec.TCellNormal
import com.gmail.jiangyang5157.sudoku.widget.terminal.render.spec.TCellSelectd
import com.gmail.jiangyang5157.sudoku_presenter.model.Terminal

/**
 * Created by Yang Jiang on July 18, 2017
 */
class TerminalView : RenderView, Renderable<Canvas> {

    companion object {
        const val TAG = "TerminalView"
    }

    private var mTerminal: TTerminal? = null

    private var mCellSelectd: Int? = null
    fun getCellSelected() = mCellSelectd
    private var mCellHighlightd: List<TCell>? = null
    private var mCellFocusd: List<TCell>? = null

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
                touch(event.x, event.y)
                refreshRender()
            }
            MotionEvent.ACTION_UP -> {
                Log.d(TAG, "MotionEvent.ACTION_UP")
                refreshRender()
            }
            MotionEvent.ACTION_MOVE -> {
                touch(event.x, event.y)
            }
        }
        return super.onTouchEvent(event)
    }

    private fun touch(x: Float, y: Float) {
        mTerminal?.apply {
            val cEdge = edge / E
            if (x in 0..edge && y in 0..edge) {
                val col = (x / cEdge).toInt()
                val row = (y / cEdge).toInt()
                if (col in 0 until E && row in 0 until E) {
                    val index = row * E + col


                    mCellFocusd?.forEach {
                        it.spec = TCellNormal
                    }
                    mCellHighlightd?.forEach {
                        it.spec = TCellNormal
                    }
                    mCellSelectd?.apply {
                        C[this].spec = TCellNormal
                    }

                    mCellFocusd = relevantCells(index)
                    mCellHighlightd = sameDigitCells(index)
                    mCellSelectd = index

                    mCellFocusd?.forEach {
                        it.spec = TCellFocusd
                    }
                    mCellHighlightd?.forEach {
                        it.spec = TCellHighlightd
                    }
                    mCellSelectd?.apply {
                        C[this].spec = TCellSelectd
                    }

                    refreshRender()
                }
            }
        }
    }

    override fun onRender(t: Canvas) {
        t.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR)
        mTerminal?.onRender(t)
    }

    fun setTerminal(p: Terminal?) {
        p?.apply {
            mTerminal = TMapper(width, height).map(p)
            mCellFocusd = null
            mCellHighlightd = null
            mCellSelectd = null
            refreshRender()
        }
    }

    fun setCell(index: Int, digit: Int?) {
        this.mTerminal?.apply {
            C[index].digit = digit ?: 0
            refreshRender()
        }
    }

    fun setPossibility(index: Int, possibility: Array<Int?>) {
        mTerminal?.apply {
            possibility.forEachIndexed { i, p ->
                C[index].P[i].digit = p ?: 0
            }
            refreshRender()
        }
    }

}