package com.gmail.jiangyang5157.sudoku.data

/**
 * Created by Yang Jiang on July 09, 2017
 */
class Terminal(val e: Int) {

    var c = arrayOfNulls<Cell>(e * e)

    override fun toString(): String {
        val sb = StringBuilder()
        sb.append("[$e]")
        for (i in c.indices) {
            if (i % e == 0) {
                sb.append("\n")
            }
            sb.append("${c[i]}, ")
        }
        sb.append("\n")
        return sb.toString()
    }

}