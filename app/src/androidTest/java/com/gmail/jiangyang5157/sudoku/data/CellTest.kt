package com.gmail.jiangyang5157.sudoku.data

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
    fun test_ToString() {
        val cell = Cell(b = 1, d = 2)
        Assert.assertEquals("[1]2/2", cell.toString())
    }

    @Test
    fun test_equals() {
        val c1 = Cell(b = 1, d = 2)
        val c2 = Cell(b = 1, d = 2)
        val c3 = Cell(b = 1, d = 3)
        Assert.assertTrue(c1 == c2)
        Assert.assertFalse(c1 == c3)
    }

}