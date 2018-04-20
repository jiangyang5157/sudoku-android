package com.gmail.jiangyang5157.sudoku_presenter.model.repo

/**
 * Created by Yang Jiang on April 20, 2018
 */
interface IndexRepoSpec {

    var mIndexes: IntArray

    fun indexes(): IntArray {
        return mIndexes
    }

}