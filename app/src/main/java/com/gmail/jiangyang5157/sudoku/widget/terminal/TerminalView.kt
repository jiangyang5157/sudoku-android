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
import com.gmail.jiangyang5157.sudoku.widget.terminal.render.spec.TCellBgHighlightd
import com.gmail.jiangyang5157.sudoku.widget.terminal.render.spec.TCellDigitHighlightd
import com.gmail.jiangyang5157.sudoku.widget.terminal.render.spec.TCellNormal
import com.gmail.jiangyang5157.sudoku_presenter.model.Terminal

/**
 * Created by Yang Jiang on July 18, 2017
 */
class TerminalView : RenderView, Renderable<Canvas> {

    companion object {
        const val TAG = "TerminalView"
    }

    private var mTerminal: TTerminal? = null

    private var mSelectdCell: Int? = null
    fun getSelectedCell() = mSelectdCell
    private var mDigitHighlightdCells: List<TCell>? = null
    private var mBgHighlightdCells: List<TCell>? = null

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
                    if (mSelectdCell != index) {
                        selectCell(index)
                        digitHighlightCell(index)
                        bgHighlightCell(index)
                        refreshRender()
                    }
                }
            }
        }
    }

    private fun selectCell(index : Int) {
        mTerminal?.apply {
            mSelectdCell?.apply { C[this].spec = TCellNormal }
            mSelectdCell = index
            mSelectdCell?.apply { C[this].spec = TCellDigitHighlightd(C[this].spec.backgroundColorInt) }
        }
    }

    private fun digitHighlightCell(index : Int) {
        mTerminal?.apply {
            mDigitHighlightdCells?.forEach { it.spec = TCellNormal }
            mDigitHighlightdCells = digitCells(C[index].digit)
            mDigitHighlightdCells?.forEach { it.spec = TCellDigitHighlightd(it.spec.backgroundColorInt) }
        }
    }

    private fun bgHighlightCell(index : Int) {
        mTerminal?.apply {
            mBgHighlightdCells?.forEach {it.spec = TCellNormal }
            mBgHighlightdCells = relevantCells(index)
            mBgHighlightdCells?.forEach { it.spec = TCellBgHighlightd(it.spec.digitColorInt) }
        }
    }

    /**
     * Calculate associated [TCell], which has same [row], or same [col], or same[TCell.B]
     */
    private fun relevantCells(index: Int): List<TCell>? {
        if (index < 0) {
            return null
        }

        mTerminal?.apply {
            val b = C[index].B
            val row = index / E
            val col = index % E
            return C.filterIndexed { i, c ->
                c.B == b || i / E == row || i % E == col
            }
        }
        return null
    }

    /**
     * Calculate associated [TCell], which has same [digit].
     */
    private fun digitCells(digit: Int): List<TCell>? {
        if (digit == 0) {
            return null
        }

        mTerminal?.apply {
            return C.filterIndexed { _, c ->
                c.digit == digit
            }
        }
        return null
    }

    override fun onRender(t: Canvas) {
        t.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR)
        mTerminal?.onRender(t)
    }

    fun setTerminal(p: Terminal?) {
        p?.apply {
            mTerminal = TMapper(width, height).map(p)
            mBgHighlightdCells = null
            mDigitHighlightdCells = null
            mSelectdCell = null
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