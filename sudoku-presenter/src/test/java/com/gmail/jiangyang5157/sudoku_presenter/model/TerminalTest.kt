package com.gmail.jiangyang5157.sudoku_presenter.model

import org.junit.Assert
import org.junit.Test

/**
 * Created by Yang Jiang on July 09, 2017'
 */
class TerminalTest {

    @Test
    fun test_toString() {
        val terminal = Terminal(2)
        terminal.C[1] = Cell(1, 2)

        Assert.assertEquals("{\"E\":2,\"C\":[null,{\"B\":1,\"D\":2},null,null]}", terminal.toString())
    }

    @Test
    fun test_equals() {
        val t1 = Terminal(1)
        val t2 = Terminal(2)
        val t3 = Terminal(2)
        val t4 = Terminal(2)
        val t5 = Terminal(2)
        val t6 = Terminal(2)

        Assert.assertTrue(t2 == t3)
        Assert.assertTrue(t2 == t4)
        Assert.assertTrue(t2 == t5)
        Assert.assertTrue(t2 == t6)
        Assert.assertTrue(t1 != t2)

        t3.C[1] = Cell(1, 1)
        t4.C[1] = Cell(1, 2)
        t5.C[1] = Cell(1, 2)
        t6.C[0] = Cell(0, 0)

        Assert.assertTrue(t2 != t3)
        Assert.assertTrue(t2 != t4)
        Assert.assertTrue(t2 != t5)
        Assert.assertTrue(t2 != t6)

        Assert.assertTrue(t3 != t4)
        Assert.assertTrue(t3 != t5)
        Assert.assertTrue(t3 != t6)

        Assert.assertTrue(t4 == t5)
        Assert.assertTrue(t4 != t6)

        Assert.assertTrue(t5 != t6)
        t5.C[0] = Cell(0, 0)
        t6.C[1] = Cell(1, 2)
        Assert.assertTrue(t5 == t6)
    }

    @Test
    fun test_copy() {
        val t1 = Terminal(1)
        val t2 = Terminal(2)
        val t3 = Terminal(2)

        val t11 = t1.copy()
        val t21 = t2.copy()
        val t22 = t2.copy(3)
        val t31 = t3.copy()

        Assert.assertTrue(t11 == t1)
        Assert.assertTrue(t21 == t2)
        Assert.assertTrue(t21 == t2)
        Assert.assertTrue(t21 == t3)
        Assert.assertTrue(t21 == t31)
        Assert.assertTrue(t22 != t2)

        t21.C[1] = Cell(1, 1)
        t22.C[1] = Cell(1, 1)

        Assert.assertTrue(t22 != t2)
        Assert.assertTrue(t21 != t31)

        t31.C[1] = Cell(1, 2)
        Assert.assertTrue(t21 != t31)

        t31.C[1] = Cell(1, 1)
        Assert.assertTrue(t21 == t31)

        Assert.assertTrue(t21.copy() == t31.copy())
    }

    @Test
    fun test_deep_copy() {
        val t1 = Terminal(2)
        t1.C[1] = Cell(1, 1)

        val t2 = t1.copy()
        Assert.assertTrue(t2 == t1)

        t1.C[1]!!.D = 10
        Assert.assertFalse(t2.C[1]!!.D == 10)
    }

    @Test
    fun test_row() {
        val t = Terminal(9)

        Assert.assertEquals(0, t.row(0))
        Assert.assertEquals(0, t.row(8))
        Assert.assertEquals(1, t.row(9))
        Assert.assertEquals(1, t.row(17))
        Assert.assertEquals(2, t.row(18))
    }

    @Test
    fun test_col() {
        val t = Terminal(9)

        Assert.assertEquals(0, t.col(0))
        Assert.assertEquals(8, t.col(8))
        Assert.assertEquals(0, t.col(9))
        Assert.assertEquals(8, t.col(17))
        Assert.assertEquals(0, t.col(18))
    }

    @Test
    fun test_index() {
        val t = Terminal(9)

        Assert.assertEquals(0, t.index(0, 0))
        Assert.assertEquals(80, t.index(8, 8))
        Assert.assertEquals(9, t.index(1, 0))
        Assert.assertEquals(17, t.index(1, 8))
        Assert.assertEquals(19, t.index(2, 1))
    }

    @Test
    fun test_up_down_left_right() {
        val t = Terminal(9)

        Assert.assertEquals(-1, t.up(0))
        Assert.assertEquals(9, t.down(0))
        Assert.assertEquals(-1, t.left(0))
        Assert.assertEquals(1, t.right(0))

//                                5,
//                                14,
//                                23,
//                        30, 31, 32,
//            36, 37, 38, 39, 40, 41, 42, 43, 44,
//                        48, 49, 50,
//                                59,
//                                68,
//                                77
        Assert.assertEquals(-1, t.up(5))
        Assert.assertEquals(14, t.down(5))

        Assert.assertEquals(68, t.up(77))
        Assert.assertEquals(-1, t.down(77))

        Assert.assertEquals(-1, t.left(36))
        Assert.assertEquals(37, t.right(36))

        Assert.assertEquals(43, t.left(44))
        Assert.assertEquals(-1, t.right(44))
    }

}