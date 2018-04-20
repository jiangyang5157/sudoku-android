package com.gmail.jiangyang5157.sudoku_presenter.model.repo

import org.junit.Assert
import org.junit.Test
import java.util.*

/**
 * Created by Yang Jiang on April 20, 2018
 */
class PossibilityRepoTest {

    @Test
    fun test_default_zero() {
        val repo = PossibilityRepo(4)
        val i0 = repo.find(PossibilityRepoSpec(intArrayOf(0)))
        Assert.assertEquals("[0, 0, 0, 0]", Arrays.toString(i0[0]))
    }

    @Test
    fun test_find_size() {
        val repo = PossibilityRepo(4)
        var i0 = repo.find(PossibilityRepoSpec(intArrayOf(0)))
        Assert.assertEquals(1, i0.size)
        i0 = repo.find(PossibilityRepoSpec(intArrayOf(0, 1, 2, 3, 4, 5, 6, 7, 8, 9)))
        Assert.assertEquals(10, i0.size)
        i0 = repo.find(PossibilityRepoSpec(intArrayOf(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15)))
        Assert.assertEquals(16, i0.size)
    }

    @Test
    fun test_update_find_copy() {
        val repo = PossibilityRepo(4)
        var i01 = repo.find(PossibilityRepoSpec(intArrayOf(0, 1)))
        Assert.assertEquals("[0, 0, 0, 0]", Arrays.toString(i01[0]))
        Assert.assertEquals("[0, 0, 0, 0]", Arrays.toString(i01[1]))
        i01[0][0] = 1
        i01[1][0] = 2

        i01 = repo.find(PossibilityRepoSpec(intArrayOf(0, 1)))
        Assert.assertEquals("[0, 0, 0, 0]", Arrays.toString(i01[0]))
        Assert.assertEquals("[0, 0, 0, 0]", Arrays.toString(i01[1]))
        i01[0][0] = 1
        i01[1][0] = 2
        repo.update(i01[0], PossibilityRepoSpec(intArrayOf(0, 1)))
        repo.update(i01[1], PossibilityRepoSpec(intArrayOf(2, 3)))

        val i0123 = repo.find(PossibilityRepoSpec(intArrayOf(0, 1, 2, 3)))
        Assert.assertEquals("[1, 0, 0, 0]", Arrays.toString(i0123[0]))
        Assert.assertEquals("[1, 0, 0, 0]", Arrays.toString(i0123[1]))
        Assert.assertEquals("[2, 0, 0, 0]", Arrays.toString(i0123[2]))
        Assert.assertEquals("[2, 0, 0, 0]", Arrays.toString(i0123[3]))
    }

}