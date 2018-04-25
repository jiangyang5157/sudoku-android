package com.gmail.jiangyang5157.sudoku.widget.terminal.render.spec

/**
 * Created by Yang Jiang on April 25, 2018
 */
class TPossibilityNormal(override var digit: Int? = null) : TPossibilitySpec {

    override val digitColorInt: Int
        get() = 0xFF424242.toInt()

}