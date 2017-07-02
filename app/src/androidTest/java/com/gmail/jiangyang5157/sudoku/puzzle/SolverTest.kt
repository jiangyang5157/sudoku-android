package com.gmail.jiangyang5157.sudoku.puzzle

import android.support.test.runner.AndroidJUnit4
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import sudoku.Sudoku

/**
 * Created by Yang Jiang on July 02, 2017
 */
@RunWith(AndroidJUnit4::class)
class SolverTest {

    // 9x9 puzzle
    val squares: Long = 3

    // 0 solution 9x9 puzzle
    val hasNone: String =
            "......123" +
                    "..9......" +
                    ".....9..." +
                    "........." +
                    "........." +
                    "........." +
                    "........." +
                    "........." +
                    "........."

    // 2 passable solutions 9x9 puzzle
    val hasTwo: String =
            "..3456789" +
                    "456789123" +
                    "789123456" +
                    "..4365897" +
                    "365897214" +
                    "897214365" +
                    "531642978" +
                    "642978531" +
                    "978531642"

    @Test
    fun test_solver_none() {
        // func SolveRaw(squares int, raw string, solutions int) string
        var result: String = Sudoku.solveRaw(squares, hasNone, 0)
        assertTrue(result.isNullOrEmpty())
        result = Sudoku.solveRaw(squares, hasNone, 1)
        assertTrue(result.isNullOrEmpty())
    }

    @Test
    fun test_solver_two() {
        val prefix = Sudoku.SOLUTION_PREFIX.toChar()
        var result: String = Sudoku.solveRaw(squares, hasTwo, 0)
        assertEquals("${prefix}123456789456789123789123456214365897365897214897214365531642978642978531978531642", result)
        result = Sudoku.solveRaw(squares, hasTwo, 1)
        assertEquals("${prefix}123456789456789123789123456214365897365897214897214365531642978642978531978531642", result)
        result = Sudoku.solveRaw(squares, hasTwo, 2)
        assertEquals("${prefix}123456789456789123789123456214365897365897214897214365531642978642978531978531642" +
                "${prefix}213456789456789123789123456124365897365897214897214365531642978642978531978531642", result)
        result = Sudoku.solveRaw(squares, hasTwo, 3)
        assertEquals("${prefix}123456789456789123789123456214365897365897214897214365531642978642978531978531642" +
                "${prefix}213456789456789123789123456124365897365897214897214365531642978642978531978531642", result)
    }
}