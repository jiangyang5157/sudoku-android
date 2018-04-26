package com.gmail.jiangyang5157.sudoku.widget.terminal.render.spec

/**
 * Created by Yang Jiang on April 25, 2018
 */
class TCellDigitHighlightd(override val backgroundColorInt: Int) : TCellSpec {

    override val priority: Int
        get() = 2

    override val digitColorInt: Int
        get() = 0xFFFF5722.toInt()

}