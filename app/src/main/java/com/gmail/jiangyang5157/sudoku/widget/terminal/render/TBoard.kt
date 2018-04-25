package com.gmail.jiangyang5157.sudoku.widget.terminal.render

import android.graphics.Canvas
import android.graphics.Paint
import com.gmail.jiangyang5157.kotlin_kit.math.Vector2i
import com.gmail.jiangyang5157.sudoku.widget.terminal.render.spec.TBoardNormal
import com.gmail.jiangyang5157.sudoku.widget.terminal.render.spec.TBoardSpec

/**
 * Created by Yang Jiang on April 25, 2018
 */
class TBoard(
        var spec: TBoardSpec = TBoardNormal(),
        override var position: Vector2i = Vector2i(),
        override var w: Int = 0,
        override var h: Int = 0,
        override var priority: Int = 0,
        override var paint: Paint = Paint())
    : TPRect {

    override fun onRender(t: Canvas) {
        val left = position.x
        val top = position.y + h
        val right = position.x + w
        val bottom = position.y

        paint.color = spec.backgroundColorInt
        t.drawRect(left.toFloat(), top.toFloat(), right.toFloat(), bottom.toFloat(), paint)
    }

}