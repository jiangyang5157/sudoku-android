package com.gmail.jiangyang5157.sudoku.widget.terminal.render.spec

import com.gmail.jiangyang5157.kotlin_kit.utils.BitFlag

/**
 * Created by Yang Jiang on April 25, 2018
 */
data class TCellSpec(
        val flag: BitFlag = BitFlag()
) {
    companion object {
        const val FIXD = 1L shl 1
        const val HIGHLIGHTD = 1L shl 2
        const val FOCUSD = 1L shl 3
    }

    val backgroundColorInt: Int
        get() = when (flag.getStatus()) {
            FIXD -> 0xFFE0F2F1.toInt()
            HIGHLIGHTD -> 0xFFE0F2F1.toInt()
            FOCUSD -> 0xFFB2DFDB.toInt()

            FIXD or HIGHLIGHTD or FOCUSD -> 0xFFB2DFDB.toInt()
            FIXD or HIGHLIGHTD  -> 0xFFE0F2F1.toInt()
            FIXD or FOCUSD  -> 0xFFB2DFDB.toInt()
            HIGHLIGHTD or FOCUSD  -> 0xFFB2DFDB.toInt()

            else -> 0xFFEEEEEE.toInt()
        }

    val digitColorInt: Int
        get() = when (flag.getStatus()) {
            FIXD -> 0xFF212121.toInt()
            HIGHLIGHTD -> 0xFFFF5722.toInt()
            FOCUSD -> 0xFF212121.toInt()

            FIXD or HIGHLIGHTD or FOCUSD -> 0xFFFF5722.toInt()
            FIXD or HIGHLIGHTD  -> 0xFFFF5722.toInt()
            FIXD or FOCUSD  -> 0xFF212121.toInt()
            HIGHLIGHTD or FOCUSD  -> 0xFFFF5722.toInt()

            else -> 0xFF212121.toInt()
        }

}