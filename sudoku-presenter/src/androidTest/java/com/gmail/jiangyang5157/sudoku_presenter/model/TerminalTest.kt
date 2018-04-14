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
        terminal.c[1] = Cell(1, 2)

        Assert.assertEquals("#2*2\nnull, #1@2:2, \nnull, null, \n", terminal.toString())
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

        t3.c[1] = Cell(1, 1)
        t4.c[1] = Cell(1, 2)
        t5.c[1] = Cell(1, 2)
        t6.c[0] = Cell(0, 0)

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
        t5.c[0] = Cell(0, 0)
        t6.c[1] = Cell(1, 2)
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

        t21.c[1] = Cell(1, 1)
        t22.c[1] = Cell(1, 1)

        Assert.assertTrue(t22 != t2)
        Assert.assertTrue(t21 != t41)

        t41.c[1] = Cell(1, 2)
        Assert.assertTrue(t21 != t41)

        t41.c[1] = Cell(1, 1)
        Assert.assertTrue(t21 == t41)

        Assert.assertTrue(t21.copy() == t41.copy())
    }

//    @Test
//    fun test_fromJson() {
//        val t1 = Terminal(1)
//        val t2 = Terminal(8)
//        val t3 = Terminal(9)
//
//        Assert.assertFalse(t1.fromJson(""))
//        Assert.assertFalse(t2.fromJson(SolverTest.TestData.terminalJson_9x9_2))
//        Assert.assertTrue(t3.fromJson(SolverTest.TestData.terminalJson_9x9_2))
//    }

}