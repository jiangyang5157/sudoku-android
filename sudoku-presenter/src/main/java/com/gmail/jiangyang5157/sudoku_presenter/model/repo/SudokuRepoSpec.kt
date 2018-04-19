package com.gmail.jiangyang5157.sudoku_presenter.model.repo

/**
 * Created by Yang Jiang on April 19, 2018
 */
interface SudokuRepoSpec {

    companion object {

        val CAPACITY: Int
            get() = 3

        val INDEX_TERMINAL: Int
            get() = 0

        val INDEX_PUZZLE: Int
            get() = 1

        val INDEX_PROGRESS: Int
            get() = 2
    }

    fun filter(): Array<Int>

}