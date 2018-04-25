package com.gmail.jiangyang5157.sudoku.widget.terminal.render.rect

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import com.gmail.jiangyang5157.kotlin_kit.math.Vector2i

/**
 * Created by Yang Jiang on April 25, 2018
 */
open class TFillRect(override var position: Vector2i = Vector2i(), override var priority: Int = 0) : TRect<TFillRectSpec, Int> {

    private val paint = Paint()

    private val rect = RectF()

    init {
        paint.style = Paint.Style.FILL
    }

    override fun onRender(t: Canvas) {
        t.drawRect(rect, paint)
    }

    override fun map(shape: TRectShape, spec: TFillRectSpec, texture: Int) {
        rect.left = (position.x + spec.margin).toFloat()
        rect.right = (position.x + shape.w - spec.margin).toFloat()
        rect.top = (position.y + shape.h - spec.margin).toFloat()
        rect.bottom = (position.y + spec.margin * 2).toFloat()

        paint.color = texture
    }
}
