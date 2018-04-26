package com.gmail.jiangyang5157.sudoku.widget.terminal.render.spec

/**
 * Created by Yang Jiang on April 25, 2018
 */
object TCellDigitHighlightd : TCellSpec {

    override val priority: Int
        get() = 3

    override val backgroundColorInt: Int
        get() = TCellTodo.backgroundColorInt

    override val digitColorInt: Int
        get() = 0xFFFF5722.toInt()

}