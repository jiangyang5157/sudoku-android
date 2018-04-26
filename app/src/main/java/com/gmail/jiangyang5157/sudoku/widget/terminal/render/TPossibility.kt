package com.gmail.jiangyang5157.sudoku.widget.terminal.render

import android.graphics.Canvas
import android.graphics.Paint
import com.gmail.jiangyang5157.kotlin_kit.math.Vector2i
import com.gmail.jiangyang5157.sudoku.widget.terminal.render.spec.TPossibilitySpec

/**
 * Created by Yang Jiang on April 25, 2018
 */
class TPossibility(
        var D: Int = 0,
        override var edge: Int = 0,
        override var position: Vector2i = Vector2i())
    : TPSquare {

    override var paint: Paint = Paint()
    var spec: TPossibilitySpec = TPossibilitySpec()

    override fun onRender(t: Canvas) {
        val left = position.x
        val top = position.y + edge

        if (D != 0) {
            paint.color = spec.digitColorInt
            val halfEdge = edge / 2
            val quarterEdge = edge / 4
            paint.textSize = (edge - quarterEdge).toFloat()
            val distance = (paint.descent() + paint.ascent()) / 2
            val textX: Float = left + halfEdge + distance
            val textY: Float = top - halfEdge - distance
            t.drawText(D.toString(), textX, textY, paint)
        }
    }

}