package com.gmail.jiangyang5157.sudoku_presenter.model.repo

import com.gmail.jiangyang5157.kotlin_kit.model.Repository

/**
 * Created by Yang Jiang on April 20, 2018
 */
class PossibilityRepo(edge: Int) : Repository<Array<Int?>, PossibilityRepoSpec> {

    private val repo: Array<Array<Int?>?> = arrayOfNulls(edge * edge)

    init {
        repo.indices.forEach { repo[it] = arrayOfNulls(edge) }
    }

    override fun add(item: Array<Int?>): Boolean {
        return false
    }

    override fun add(items: Iterable<Array<Int?>>): Boolean {
        return false
    }

    override fun find(spec: PossibilityRepoSpec): List<Array<Int?>> {
        val ret = arrayListOf<Array<Int?>>()
        spec.indexes().forEach { repo[it]?.let { it2 -> ret.add(it2) } }
        return ret
    }

    override fun remove(spec: PossibilityRepoSpec): Boolean {
        spec.indexes().forEach { repo[it]?.map { null } }
        return true
    }

    override fun update(item: Array<Int?>, spec: PossibilityRepoSpec): Boolean {
        spec.indexes().forEach { repo[it] = item.copyOf() }
        return true
    }

}
