package com.gmail.jiangyang5157.sudoku_presenter.model.repo

import com.gmail.jiangyang5157.kotlin_kit.model.Repository

/**
 * Created by Yang Jiang on April 20, 2018
 */
class PossibilityRepo(edge: Int) : Repository<IntArray, PossibilityRepoSpec> {

    private val repo: Array<IntArray> = Array(edge * edge) { IntArray(edge) }

    override fun add(item: IntArray): Boolean {
        return false
    }

    override fun add(items: Iterable<IntArray>): Boolean {
        return false
    }

    override fun find(spec: PossibilityRepoSpec): List<IntArray> {
        val ret = arrayListOf<IntArray>()
        spec.indexes().forEach { repo[it].let { it2 -> ret.add(it2.clone()) } }
        return ret
    }

    override fun remove(spec: PossibilityRepoSpec): Boolean {
        spec.indexes().forEach { repo[it] = IntArray(repo[it].size) }
        return true
    }

    override fun update(item: IntArray, spec: PossibilityRepoSpec): Boolean {
        spec.indexes().forEach { repo[it] = item.copyOf() }
        return true
    }

}
