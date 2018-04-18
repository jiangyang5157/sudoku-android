package com.gmail.jiangyang5157.sudoku_presenter.model

import com.gmail.jiangyang5157.kotlin_core.model.Repository

/**
 * Created by Yang Jiang on April 19, 2018
 */
class SudokuRepository : Repository<Terminal, SudokuSpec> {

    /**
     * Size of 2 [Terminal] array hold original puzzle [Terminal] and progress [Terminal]
     */
    private var mSudoku = arrayOfNulls<Terminal>(2)

    override fun add(item: Terminal): Boolean {
        mSudoku[0] = item
        mSudoku[1] = item.copy()
        return true
    }

    override fun add(items: Iterable<Terminal>): Boolean {
        return false
    }

    override fun find(spec: SudokuSpec): List<Terminal> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun remove(spec: SudokuSpec): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun update(spec: SudokuSpec): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}