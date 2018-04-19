package com.gmail.jiangyang5157.sudoku_presenter.model.repo

import com.gmail.jiangyang5157.kotlin_kit.model.Repository
import com.gmail.jiangyang5157.sudoku_presenter.model.Terminal

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
        val ret = arrayListOf<Terminal>()
        spec.filter().forEach { repo[it]?.let { t -> ret.add(t) } }
        return ret
    }

    override fun remove(spec: SudokuRepoSpec): Boolean {
        spec.filter().forEach { repo[it] = null }
        return true
    }

    override fun update(item: Terminal, spec: SudokuRepoSpec): Boolean {
        spec.filter().forEach { repo[it] = item.copy() }
        return true
    }

}