package com.gmail.jiangyang5157.sudoku.widget.terminal.render.spec

import com.gmail.jiangyang5157.kotlin_kit.utils.BitFlag

/**
 * Created by Yang Jiang on April 25, 2018
 */
data class TPossibilitySpec(
        val flag: BitFlag = BitFlag()
) {

    val digitColorInt: Int
        get() = 0xFF424242.toInt()

}