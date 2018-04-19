package com.gmail.jiangyang5157.sudoku_presenter.lib

import org.junit.Assert
import org.junit.Test
import sudoku.Sudoku

/**
 * Created by Yang Jiang on April 19, 2018
 */
class SudokuGeneratorTest {

    @Test
    fun test_generator() {
        val tBytes = Sudoku.genByte(0, 9, 4, 55)
        Assert.assertNotNull(tBytes)
        Assert.assertTrue(tBytes.isNotEmpty())
        Assert.assertNotEquals("null", tBytes)
        val tString = Sudoku.genString(0, 9, 4, 55)
        Assert.assertNotNull(tString)
        Assert.assertTrue(tString.isNotEmpty())
        Assert.assertNotEquals("null", tString)
    }

}