package com.gmail.jiangyang5157.sudoku.data

/**
 * Created by Yang Jiang on July 13, 2017
 */
class Cell(val b: Int, val d: Int) {

    var guess = d

    override fun toString(): String {
        return "[$b]$d/$guess"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other?.javaClass != javaClass) return false

        other as Cell

        if (b != other.b) return false
        if (d != other.d) return false
        if (guess != other.guess) return false

        return true
    }

    override fun hashCode(): Int {
        var ret = 1
        val prime = 37
        ret = prime * ret + (b.toLong() xor b.toLong().ushr(32)).toInt()
        ret = prime * ret + (d.toLong() xor d.toLong().ushr(32)).toInt()
        ret = prime * ret + (guess.toLong() xor guess.toLong().ushr(32)).toInt()
        return ret
    }
}
