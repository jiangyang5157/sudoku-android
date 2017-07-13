package com.gmail.jiangyang5157.sudoku.data

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
    fun test_ToString() {
        val terminal = Terminal(2)
        terminal.c[1] = Cell(1, 2)
        Assert.assertEquals("[2]\nnull, [1]2/2, \nnull, null, \n", terminal.toString())
    }

}