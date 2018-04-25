package com.gmail.jiangyang5157.sudoku.widget.terminal.render

import android.graphics.Canvas
import android.graphics.Paint
import com.gmail.jiangyang5157.kotlin_kit.math.Vector2i

/**
 * Created by Yang Jiang on April 25, 2018
 */
class TBoard(
        override var position: Vector2i = Vector2i(),
        override var w: Int = 0,
        override var h: Int = 0,
        override var priority: Int = 0,
        override var paint: Paint = Paint())
    : TPRect, TRenderable {

    override fun onRender(t: Canvas) {
        val left = position.x
        val top = position.y + h
        val right = position.x + w
        val bottom = position.y

        paint.style = Paint.Style.STROKE
        paint.strokeWidth = 20F
        paint.color = 0xFFFF0000.toInt()
        t.drawRect(left.toFloat(), top.toFloat(), right.toFloat(), bottom.toFloat(), paint)

//        scale(-20)
//
//        paint.style = Paint.Style.FILL
//        paint.color = 0xFF000FFF.toInt()
//        t.drawRect(left.toFloat(), top.toFloat(), right.toFloat(), bottom.toFloat(), paint)
//
//        scale(200)
    }

}