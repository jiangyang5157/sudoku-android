package com.gmail.jiangyang5157.sudoku_presenter.model

import com.gmail.jiangyang5157.kotlin_kit.utils.IoUtils
import com.google.gson.Gson
import java.util.*

/**
 * Created by Yang Jiang on July 09, 2017
 */
data class Terminal(

        /**
         * Edge length of the [Terminal]
         */
        val E: Int,

        /**
         * Order-sensitive [Array] of [Cell]: left-to-right(columns) && up-to-down(rows)
         */
        val C: Array<Cell?> = arrayOfNulls(E * E)
) {

    /**
     * To human readable [String]
     */
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

    /**
     * To JSON [String]
     */
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

    /**
     * Deep copy
     */
    fun copy(): Terminal {
        val newTerminal = Terminal(E = E)
        C.forEachIndexed { index, cell -> newTerminal.C[index] = cell?.copy() }
        return newTerminal
    }

    fun row(index: Int) = index / E

    fun col(index: Int) = index % E

    fun index(row: Int, col: Int) = row * E + col

    fun cell(row: Int, col: Int) = C[index(row, col)]

    /**
     * Get `up` index at current [index], returns -1 if not found.
     */
    fun up(index: Int): Int {
        val ret = index - E
        if (ret < 0) {
            return -1
        }
        return ret
    }

    /**
     * Get `down` index at current [index], returns -1 if not found.
     */
    fun down(index: Int): Int {
        val ret = index + E
        if (ret > E - 1) {
            return -1
        }
        return ret
    }

    /**
     * Get `left` index at current [index], returns -1 if not found.
     */
    fun left(index: Int): Int {
        val ret = index - 1
        if (ret < 0 || row(ret) != row(index)) {
            return -1
        }
        return ret
    }

    /**
     * Get `right` index at current [index], returns -1 if not found.
     */
    fun right(index: Int): Int {
        val ret = index + 1
        if (ret > E - 1 || row(ret) != row(index)) {
            return -1
        }
        return ret
    }

    /**
     * Get `neighbour` indexes at current [index].
     */
    fun neighbours(index: Int): List<Int> {
        return intArrayOf(up(index), down(index), left(index), left(index)).filter { it != -1 }
    }
}