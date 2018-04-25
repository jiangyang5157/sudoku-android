package com.gmail.jiangyang5157.sudoku.widget.terminal.render

import android.graphics.Canvas
import android.graphics.Paint
import com.gmail.jiangyang5157.kotlin_kit.math.Vector2i
import com.gmail.jiangyang5157.sudoku.widget.terminal.render.spec.TPossibilityNormal
import com.gmail.jiangyang5157.sudoku.widget.terminal.render.spec.TPossibilitySpec

/**
 * Created by Yang Jiang on April 25, 2018
 */
class TPossibility(
        override var position: Vector2i = Vector2i(),
        override var w: Int = 0,
        override var h: Int = 0,
        var spec: TPossibilitySpec = TPossibilityNormal(),
        override var paint: Paint = Paint())
    : TPRect {

    override fun onRender(t: Canvas) {
        val left = position.x
        val top = position.y + h
        val right = position.x + w
        val bottom = position.y

        if (spec.digit != 0) {
            paint.color = spec.digitColorInt
            paint.textSize = ((w + h) / 4).toFloat()
            val distance = (paint.descent() + paint.ascent()) / 2
            val textX: Float = left + w / 2 - distance
            val textY: Float = top - h / 2 - distance
            t.drawText(spec.digit.toString(), textX, textY, paint)
        }
    }

}