package com.gmail.jiangyang5157.sudoku_presenter.model

import android.support.test.runner.AndroidJUnit4
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Created by Yang Jiang on July 09, 2017'
 */
@RunWith(AndroidJUnit4::class)
class TerminalTest {

    @Test
    fun test_toString() {
        val terminal = Terminal(2)
        terminal.C[1] = Cell(1, 2)

        Assert.assertEquals("{\"C\":[null,{\"B\":1,\"D\":2},null,null],\"E\":2}", terminal.toString())
    }

    @Test
    fun test_equals() {
        val t1 = Terminal(1)
        val t2 = Terminal(2)
        val t3 = Terminal(2)
        val t4 = Terminal(2)
        val t5 = Terminal(2)
        val t6 = Terminal(2)
        val t7 = t6

        Assert.assertTrue(t2 == t3)
        Assert.assertTrue(t2 == t4)
        Assert.assertTrue(t2 == t5)
        Assert.assertTrue(t2 == t6)
        Assert.assertTrue(t2 == t7)
        Assert.assertTrue(t1 != t2)

        t3.C[1] = Cell(1, 1)
        t4.C[1] = Cell(1, 2)
        t5.C[1] = Cell(1, 2)
        t6.C[0] = Cell(0, 0)

        Assert.assertTrue(t2 != t3)
        Assert.assertTrue(t2 != t4)
        Assert.assertTrue(t2 != t5)
        Assert.assertTrue(t2 != t6)
        Assert.assertTrue(t2 != t7)

        Assert.assertTrue(t3 != t4)
        Assert.assertTrue(t3 != t5)
        Assert.assertTrue(t3 != t6)

        Assert.assertTrue(t4 == t5)
        Assert.assertTrue(t4 != t6)

        Assert.assertTrue(t5 != t6)
        Assert.assertTrue(t5 != t7)
        t5.C[0] = Cell(0, 0)
        t6.C[1] = Cell(1, 2)
        Assert.assertTrue(t5 == t6)
        Assert.assertTrue(t5 == t7)
    }

    @Test
    fun test_copy() {
        val t1 = Terminal(1)
        val t2 = Terminal(2)
        val t3 = Terminal(2)
        val t4 = Terminal(2)

        val t11 = t1.copy()
        val t21 = t2.copy()
        val t22 = t2.copy(3)
        val t41 = t4.copy()

        Assert.assertTrue(t11 == t1)
        Assert.assertTrue(t21 == t2)
        Assert.assertTrue(t21 == t2)
        Assert.assertTrue(t21 == t3)
        Assert.assertTrue(t21 == t4)
        Assert.assertTrue(t21 == t41)
        Assert.assertTrue(t22 != t2)

        t21.C[1] = Cell(1, 1)
        t22.C[1] = Cell(1, 1)

        Assert.assertTrue(t22 != t2)
        Assert.assertTrue(t21 != t41)

        t41.C[1] = Cell(1, 2)
        Assert.assertTrue(t21 != t41)

        t41.C[1] = Cell(1, 1)
        Assert.assertTrue(t21 == t41)

        Assert.assertTrue(t21.copy() == t41.copy())
    }

}