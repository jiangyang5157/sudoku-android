package com.gmail.jiangyang5157.sudoku.widget.terminal.render

import android.graphics.Canvas
import android.graphics.Paint
import com.gmail.jiangyang5157.kotlin_kit.math.Vector2i
import com.gmail.jiangyang5157.sudoku.widget.terminal.render.spec.TTerminalNormal
import com.gmail.jiangyang5157.sudoku.widget.terminal.render.spec.TTerminalSpec

/**
 * Created by Yang Jiang on April 25, 2018
 */
class TTerminal(
        val E: Int,
        val C: Array<TCell> = Array(E * E) { TCell(E) },
        override var position: Vector2i = Vector2i(),
        override var edge: Int = 0,
        var spec: TTerminalSpec = TTerminalNormal,
        override var paint: Paint = Paint())
    : TPSquare {

    override fun onRender(t: Canvas) {
        val left = position.x
        val top = position.y + edge
        val right = position.x + edge
        val bottom = position.y

        spec.backgroundColorInt.apply {
            paint.color = this
            t.drawRect(left.toFloat(), top.toFloat(), right.toFloat(), bottom.toFloat(), paint)
        }

        C.forEach { it.onRender(t) }
    }

    /**
     * Calculate associated [TCell], which has same [row], or same [col], or same[TCell.B]
     *
     * @param index index of the [TCell]
     * @return [List] of relevant [TCell], itself is included
     */
    fun relevantCells(index: Int): List<TCell> {
        if (index < 0) {
            throw IllegalArgumentException()
        }

        val b = C[index].B
        val row = row(index)
        val col = col(index)
        return C.filterIndexed { i, c ->
            c.B == b || row(i) == row || col(i) == col
        }
    }

    /**
     * Calculate associated [TCell], which has same [TCell.digit]
     *
     * @param index index of the [TCell]
     * @return [List] of relevant [TCell], itself is included
     */
    fun sameDigitCells(index: Int): List<TCell> {
        if (index < 0) {
            throw IllegalArgumentException()
        }

        val d = C[index].digit
        return C.filterIndexed { _, c ->
            c.digit == d && c.digit != 0
        }
    }

    fun row(index: Int) = index / E

    fun col(index: Int) = index % E

}