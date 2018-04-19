package com.gmail.jiangyang5157.sudoku_presenter.model.repo

/**
 * Created by Yang Jiang on April 19, 2018
 */
class PuzzleFilter : SudokuRepoSpec {

    override fun filter(): Array<Int> {
        return arrayOf(SudokuRepoSpec.INDEX_PUZZLE)
    }

}