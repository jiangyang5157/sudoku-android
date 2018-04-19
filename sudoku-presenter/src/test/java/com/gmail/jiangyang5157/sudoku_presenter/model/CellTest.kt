package com.gmail.jiangyang5157.sudoku_presenter.model

import org.junit.Assert
import org.junit.Test

/**
 * Created by Yang Jiang on July 13, 2017
 */
class CellTest {

    @Test
    fun test_toString() {
        val cell = Cell(B = 1, D = 2)

        Assert.assertEquals("{\"B\":1,\"D\":2}", cell.toString())
    }

    @Test
    fun test_equals() {
        val c1 = Cell(B = 1, D = 2)
        val c2 = Cell(B = 1, D = 2)
        val c3 = Cell(B = 1, D = 3)

        Assert.assertTrue(c1 == c2)
        Assert.assertTrue(c1 != c3)
    }

    @Test
    fun test_copy() {
        val c1 = Cell(B = 1, D = 2)
        val c2 = Cell(B = 1, D = 2)
        val c3 = Cell(B = 1, D = 3)

        val c11 = c1.copy()
        val c12 = c1.copy(1, 3)
        val c21 = c2.copy()
        val c31 = c3.copy()

        Assert.assertTrue(c11 == c1)
        Assert.assertTrue(c11 == c21)
        Assert.assertTrue(c12 == c3)
        Assert.assertTrue(c12 == c31)
        Assert.assertTrue(c12 != c1)
        Assert.assertTrue(c12 != c11)
    }

}