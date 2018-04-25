package com.gmail.jiangyang5157.sudoku.widget.terminal.render

import com.gmail.jiangyang5157.kotlin_kit.math.Vector2i
import com.gmail.jiangyang5157.sudoku.widget.terminal.render.rect.TRectShape
import com.gmail.jiangyang5157.sudoku.widget.terminal.render.rect.TStrokeRect
import com.gmail.jiangyang5157.sudoku.widget.terminal.render.rect.TStrokeRectSpec

/**
 * Created by Yang Jiang on April 25, 2018
 */
class TBoard(position: Vector2i = Vector2i(),
             priority: Int = 0)
    : TStrokeRect(position, priority) {

    init {
        map(
                TRectShape(100, 100),
                TStrokeRectSpec(2, 2f),
                0xFFFF0000.toInt()
        )
    }

    override fun map(shape: TRectShape, spec: TStrokeRectSpec, texture: Int) {
        super.map(shape, spec, texture)
    }
}
