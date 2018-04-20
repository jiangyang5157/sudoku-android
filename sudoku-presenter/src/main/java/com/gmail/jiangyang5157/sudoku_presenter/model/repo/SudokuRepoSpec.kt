package com.gmail.jiangyang5157.sudoku_presenter.model.repo

/**
 * Created by Yang Jiang on April 19, 2018
 */
class SudokuRepoSpec(override var mIndexes: Array<Int>) : IndexRepoSpec {

    companion object {

        const val CAPACITY = 3

        const val INDEX_TERMINAL = 0

        const val INDEX_PUZZLE = 1

        const val INDEX_PROGRESS = 2
    }

}