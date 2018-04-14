package com.gmail.jiangyang5157.sudoku_presenter.model

import com.google.gson.Gson

/**
 * Created by Yang Jiang on July 13, 2017
 */
data class Cell(
        val B: Int,
        var D: Int = 0
) {

    override fun toString(): String {
        return Gson().toJson(this)
    }

}
