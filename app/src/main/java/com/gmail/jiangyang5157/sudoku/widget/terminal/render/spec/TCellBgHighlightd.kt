package com.gmail.jiangyang5157.sudoku.widget.terminal.render.spec

/**
 * Created by Yang Jiang on April 25, 2018
 */
class TCellBgHighlightd(override val digitColorInt: Int) : TCellSpec {

    override val priority: Int
        get() = 1

    override val backgroundColorInt: Int
        get() = 0xFFB2DFDB.toInt()

}