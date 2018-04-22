package com.gmail.jiangyang5157.sudoku_presenter.model

import org.junit.Assert
import org.junit.Test

/**
 * Created by Yang Jiang on April 22, 2018
 */
class PossibilityTerminalTest {

    @Test
    fun test_deep_copy() {
        val p1 = PossibilityTerminal(Terminal(9))
        p1.P[1][1] = 1
        p1.P[1][2] = 2
        p1.P[1][3] = 3

        val p2 = p1.copy()
        Assert.assertTrue(p1 == p2)
        Assert.assertTrue(p1.P[1][1] == p2.P[1][1])
        Assert.assertTrue(p1.P[1][1] != p2.P[1][2])

        p1.P[1][1] = 10
        Assert.assertTrue(p2.P[1][1] != 10)
        Assert.assertEquals(9, p1.P[1].size)
        Assert.assertEquals(9, p2.P[1].size)

        p1.P[1][3] = null
        Assert.assertTrue(p2.P[1][3] != null)

        val p3 = p1.copy()
        Assert.assertTrue(p1 == p3)
        Assert.assertEquals(9, p1.P[1].size)
        Assert.assertEquals(9, p3.P[1].size)
        Assert.assertEquals(null, p1.P[1][3])
        Assert.assertEquals(null, p3.P[1][3])
    }

}