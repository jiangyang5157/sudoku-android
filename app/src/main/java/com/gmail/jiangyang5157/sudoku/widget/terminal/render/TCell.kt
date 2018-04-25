package com.gmail.jiangyang5157.sudoku.widget.terminal.render

import android.graphics.Canvas
import android.graphics.Paint
import com.gmail.jiangyang5157.kotlin_kit.math.Vector2i
import com.gmail.jiangyang5157.sudoku.widget.terminal.render.spec.TCellNormal
import com.gmail.jiangyang5157.sudoku.widget.terminal.render.spec.TCellSpec

/**
 * Created by Yang Jiang on April 25, 2018
 */
class TCell(
        val E: Int,
        val P: Array<TPossibility> = Array(E) { TPossibility() },
        override var position: Vector2i = Vector2i(),
        override var w: Int = 0,
        override var h: Int = 0,
        var spec: TCellSpec = TCellNormal(),
        override var paint: Paint = Paint())
    : TPRect {

    override fun onRender(t: Canvas) {
        val left = position.x
        val top = position.y + h
        val right = position.x + w
        val bottom = position.y

        paint.color = spec.backgroundColorInt
        t.drawRect(left.toFloat(), top.toFloat(), right.toFloat(), bottom.toFloat(), paint)

        if (spec.digit != 0) {
            paint.color = spec.digitColorInt
            paint.textSize = ((w + h) / 4).toFloat()
            val textX: Float = (left + w / 2).toFloat()
            val textY: Float = top - h / 2 - (paint.descent() + paint.ascent()) / 2
            t.drawText(spec.digit.toString(), textX, textY, paint)
        }

        P.forEach { it.onRender(t) }
    }

}