package com.gmail.jiangyang5157.sudoku_presenter.model

import com.gmail.jiangyang5157.kotlin_kit.utils.IoUtils
import com.google.gson.Gson
import java.util.*

/**
 * Created by Yang Jiang on July 09, 2017
 */
data class Terminal(
        val E: Int,
        val C: Array<Cell?> = arrayOfNulls(E * E)
) {

    fun toSquareString(): String {
        val LINE_SEPARATOR = IoUtils.lineSeparator()
        val SPACE = " "
        val INT_FORMAT = 16
        val buf = StringBuffer()
        for (i in 0 until C.size) {
            if (i % E == 0) {
                buf.append(LINE_SEPARATOR)
            } else {
                buf.append(SPACE)
            }
            buf.append(C[i]?.D?.toString(INT_FORMAT))
        }
        return buf.toString()
    }

    override fun toString(): String {
        return Gson().toJson(this)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Terminal

        if (E != other.E) return false
        if (!Arrays.equals(C, other.C)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = E
        result = 31 * result + Arrays.hashCode(C)
        return result
    }

    fun copy(): Terminal {
        val newTerminal = Terminal(E = E)
        C.forEachIndexed { index, cell -> newTerminal.C[index] = cell?.copy() }
        return newTerminal
    }

}