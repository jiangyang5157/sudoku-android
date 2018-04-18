package com.gmail.jiangyang5157.sudoku_presenter.model

import com.gmail.jiangyang5157.kotlin_core.model.Repository

/**
 * Created by Yang Jiang on April 19, 2018
 */
class SudokuRepository : Repository<Terminal, SudokuSpec> {

    private var mSudoku = arrayOfNulls<Terminal>(SudokuSpec.CAPACITY)

    override fun add(item: Terminal): Boolean {
        return false
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
//        mSudoku[SudokuSpec.INDEX_PUZZLE] = item
//        mSudoku[SudokuSpec.INDEX_PROGRESS] = item.copy()
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}