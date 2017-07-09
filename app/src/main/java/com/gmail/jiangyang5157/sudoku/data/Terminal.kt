package com.gmail.jiangyang5157.sudoku.data

/**
 * Created by Yang Jiang on July 09, 2017
 */
class Terminal() {

    val edge: Int = 0

    override fun toString(): String {
        return "TODO Terminal.toString"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other?.javaClass != javaClass) return false

        other as Terminal

        // TODO
//        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        // TODO
//        return id?.hashCode() ?: 0
        return 0
    }
}