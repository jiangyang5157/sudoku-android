package com.gmail.jiangyang5157.sudoku_presenter.model

import java.util.*

/**
 * Created by Yang Jiang on April 22, 2018
 */
data class PossibilityTerminal(

        val T: Terminal,

        /**
         * Order-sensitive [Array] of possibility of [Cell.D] for each [Cell] in the [T]
         */
        val P: Array<Array<Int?>> = Array(T.E * T.E) { arrayOfNulls<Int>(T.E) }
) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as PossibilityTerminal

        if (T != other.T) return false

        for (i in 0 until P.size) {
            if (!Arrays.equals(P[i], other.P[i])) return false
        }

        return true
    }

    override fun hashCode(): Int {
        var result = T.hashCode()
        for (i in 0 until P.size) {
            result = 31 * result + Arrays.hashCode(P[i])
        }
        return result
    }

    /**
     * Deep copy
     */
    fun copy(): PossibilityTerminal {
        val newPossibilityTerminal = PossibilityTerminal(T = T.copy())
        for (i in 0 until P.size) {
            newPossibilityTerminal.P[i] = P[i].clone()
        }
        return newPossibilityTerminal
    }

}