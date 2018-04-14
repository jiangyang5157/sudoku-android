package com.gmail.jiangyang5157.sudoku_presenter.model

/**
 * Created by Yang Jiang on July 13, 2017
 */
data class Cell(val b: Int, val d: Int) {

    var value = d

    override fun toString(): String {
        return "#$b@$d:$value"
    }

}
