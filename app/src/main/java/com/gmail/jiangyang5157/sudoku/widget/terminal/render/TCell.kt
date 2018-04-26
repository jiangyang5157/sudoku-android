package com.gmail.jiangyang5157.sudoku.widget.terminal.render

import android.graphics.Canvas
import android.graphics.Paint
import com.gmail.jiangyang5157.kotlin_kit.math.Vector2i
import com.gmail.jiangyang5157.sudoku.widget.terminal.render.spec.TCellSpec

/**
 * Created by Yang Jiang on April 25, 2018
 */
class TCell(
        val P: Array<TPossibility>,
        var B: Int = 0,
        var D: Int = 0,
        override var edge: Int = 0,
        override var position: Vector2i = Vector2i())
    : TPSquare {

    override var paint: Paint = Paint()
    val spec: TCellSpec = TCellSpec()

    override fun onRender(t: Canvas) {
        val left = position.x
        val top = position.y + edge
        val right = position.x + edge
        val bottom = position.y

        spec.backgroundColorInt.apply {
            paint.color = this
            t.drawRect(left.toFloat(), top.toFloat(), right.toFloat(), bottom.toFloat(), paint)
        }

        if (D != 0) {
            paint.color = spec.digitColorInt
            val halfEdge = edge / 2
            paint.textSize = halfEdge.toFloat()
            val distance = (paint.descent() + paint.ascent()) / 2
            val textX: Float = left + halfEdge + distance
            val textY: Float = top - halfEdge - distance
            t.drawText(D.toString(), textX, textY, paint)
        }

        P.forEach { it.onRender(t) }
    }

}