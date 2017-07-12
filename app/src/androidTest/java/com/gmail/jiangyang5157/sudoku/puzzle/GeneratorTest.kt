package com.gmail.jiangyang5157.sudoku.puzzle

import android.support.test.runner.AndroidJUnit4
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import sudoku.Sudoku

/**
 * Created by Yang Jiang on July 02, 2017
 */
@RunWith(AndroidJUnit4::class)
class GeneratorTest {

    @Test
    fun test_generator() {
        val tBytes = Sudoku.genByte(9, 4, 55)
        Assert.assertNotNull(tBytes)
        Assert.assertTrue(tBytes.isNotEmpty())
        Assert.assertNotEquals("null", tBytes)
        val tString = Sudoku.genString(9, 4, 55)
        Assert.assertNotNull(tString)
        Assert.assertTrue(tString.isNotEmpty())
        Assert.assertNotEquals("null", tString)
    }

}