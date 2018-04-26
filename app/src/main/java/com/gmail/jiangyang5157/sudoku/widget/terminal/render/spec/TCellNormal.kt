package com.gmail.jiangyang5157.sudoku.widget.terminal.render.spec

/**
 * Created by Yang Jiang on April 25, 2018
 */
object TCellNormal : TCellSpec {

    override val priority: Int
        get() = 0

    override val backgroundColorInt: Int
        get() = 0xFFE0F2F1.toInt()


    override val digitColorInt: Int
        get() = 0xFF212121.toInt()

}