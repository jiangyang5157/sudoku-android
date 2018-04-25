package com.gmail.jiangyang5157.sudoku.widget.terminal.render.spec

/**
 * Created by Yang Jiang on April 25, 2018
 */
class TCellRelevant(override var digit: Int = 0) : TCellSpec {

    override val backgroundColorInt: Int
        get() = 0xFF80CBC4.toInt()


    override val digitColorInt: Int
        get() = 0xFF212121.toInt()

}