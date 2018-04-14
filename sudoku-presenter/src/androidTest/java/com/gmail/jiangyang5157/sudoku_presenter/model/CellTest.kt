package com.gmail.jiangyang5157.sudoku_presenter.model

import android.support.test.runner.AndroidJUnit4
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Created by Yang Jiang on July 13, 2017
 */

@RunWith(AndroidJUnit4::class)
class CellTest {

    @Test
    fun test_toString() {
        val cell = Cell(b = 1, d = 2)

        Assert.assertEquals("#1@2:2", cell.toString())
    }

    @Test
    fun test_equals() {
        val c1 = Cell(b = 1, d = 2)
        val c2 = Cell(b = 1, d = 2)
        val c3 = Cell(b = 1, d = 3)

        Assert.assertTrue(c1 == c2)
        Assert.assertTrue(c1 != c3)
    }

    @Test
    fun test_copy() {
        val c1 = Cell(b = 1, d = 2)
        val c2 = Cell(b = 1, d = 2)
        val c3 = Cell(b = 1, d = 3)

        val c11 = c1.copy()
        val c12 = c1.copy(1,3)
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