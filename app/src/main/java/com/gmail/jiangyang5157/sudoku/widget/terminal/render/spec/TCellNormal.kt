package com.gmail.jiangyang5157.sudoku.widget.terminal.render.spec

/**
 * Created by Yang Jiang on April 25, 2018
 */
class TCellNormal(override var digit: Int? = null) : TCellSpec {

    override val backgroundColorInt: Int
        get() = 0xFFF5F5F5.toInt()


    override val digitColorInt: Int
        get() = 0xFF212121.toInt()

}