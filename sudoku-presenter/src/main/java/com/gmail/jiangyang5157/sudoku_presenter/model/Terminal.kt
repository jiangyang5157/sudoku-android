package com.gmail.jiangyang5157.sudoku_presenter.model

import com.google.gson.Gson
import java.util.*

/**
 * Created by Yang Jiang on July 09, 2017
 */
data class Terminal(
        val E: Int,
        val C: Array<Cell?> = arrayOfNulls(E * E)
) {

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

}