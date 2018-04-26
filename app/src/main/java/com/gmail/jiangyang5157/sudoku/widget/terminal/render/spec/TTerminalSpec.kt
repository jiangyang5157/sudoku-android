package com.gmail.jiangyang5157.sudoku.widget.terminal.render.spec

import com.gmail.jiangyang5157.kotlin_kit.utils.BitFlag

/**
 * Created by Yang Jiang on April 25, 2018
 */
data class TTerminalSpec(
        val flag: BitFlag = BitFlag()
) {

    val backgroundColorInt: Int
        get() = 0xFFF5F5F5.toInt()

}