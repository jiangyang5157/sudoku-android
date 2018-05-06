package com.gmail.jiangyang5157.sudoku_classifier

import android.graphics.Canvas
import android.graphics.Paint
import com.gmail.jiangyang5157.kotlin_kit.render.Renderable

/**
 * Created by Yang Jiang on May 06, 2018
 */
class FrameText(size: Float, color: Int, var x: Float = 0.0f, var y: Float = 0.0f)
    : Renderable<Canvas> {

    private val paint = Paint().apply {
        this.textSize = size
        this.color = color
        this.style = Paint.Style.FILL
        this.isAntiAlias = false
        this.alpha = 255
    }

    var lines = arrayListOf<String>()

    override fun onRender(t: Canvas) {
        lines.forEachIndexed { i, line ->
            t.drawText(line, x, y - paint.textSize * (lines.size - i - 1), paint)
        }
    }

}