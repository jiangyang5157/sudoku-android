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

    // 9x9 puzzle
    val squares: Long = 3

    @Test
    fun test_generator() {
        // func GeneratePuzzle(squares int, minSubGiven int, minTotalGiven int) string
        val result = Sudoku.generatePuzzle(squares, 1, 23)
        Assert.assertFalse(result.isNullOrEmpty())
    }
}