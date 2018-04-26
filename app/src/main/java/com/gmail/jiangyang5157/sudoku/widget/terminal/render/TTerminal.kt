package com.gmail.jiangyang5157.sudoku.widget.terminal.render

import android.graphics.Canvas
import android.graphics.Paint
import com.gmail.jiangyang5157.kotlin_kit.math.Vector2i
import com.gmail.jiangyang5157.sudoku.widget.terminal.render.spec.TTerminalSpec

/**
 * Created by Yang Jiang on April 25, 2018
 */
class TTerminal(
        val E: Int,
        val C: Array<TCell> = Array(E * E) { TCell(P = Array(E) { TPossibility() }) },
        override var edge: Int = 0,
        override var position: Vector2i = Vector2i())
    : TPSquare {

    override var paint: Paint = Paint()
    var spec: TTerminalSpec = TTerminalSpec()

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

}