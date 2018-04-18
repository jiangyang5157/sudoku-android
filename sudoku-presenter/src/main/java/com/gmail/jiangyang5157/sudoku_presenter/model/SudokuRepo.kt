package com.gmail.jiangyang5157.sudoku_presenter.model

import com.gmail.jiangyang5157.kotlin_core.model.Repository

/**
 * Created by Yang Jiang on April 19, 2018
 */
class SudokuRepo : Repository<Terminal, SudokuRepoSpec> {

    private var repo = arrayOfNulls<Terminal>(SudokuRepoSpec.CAPACITY)

    override fun add(item: Terminal): Boolean {
        return false
    }

    override fun add(items: Iterable<Terminal>): Boolean {
        return false
    }

    override fun find(spec: SudokuRepoSpec): List<Terminal> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun remove(spec: SudokuRepoSpec): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun update(item: Terminal, spec: SudokuRepoSpec): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}