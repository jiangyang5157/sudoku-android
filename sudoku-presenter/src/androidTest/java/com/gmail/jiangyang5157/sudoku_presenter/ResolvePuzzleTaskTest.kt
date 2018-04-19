package com.gmail.jiangyang5157.sudoku_presenter

import android.support.test.runner.AndroidJUnit4
import com.gmail.jiangyang5157.sudoku_presenter.model.Terminal
import com.gmail.jiangyang5157.sudoku_presenter.puzzle.SudokuSolverTest
import com.google.gson.Gson
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import sudoku.Sudoku
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

/**
 * Created by Yang Jiang on April 14, 2018
 */
@RunWith(AndroidJUnit4::class)
class ResolvePuzzleTaskTest {

    @Test
    fun test_execute() {
        val signal = CountDownLatch(1)
        var t1: Terminal? = null
        val t2: Terminal = Gson().fromJson(SudokuSolverTest.TestData.terminalJson_9x9_2, Terminal::class.java)

        ResolvePuzzleTask(object : PuzzleTask.Callback {
            override fun onResult(result: Terminal?) {
                t1 = result
                signal.countDown()
            }
        }).execute(t2)
        signal.await(10, TimeUnit.SECONDS)
        val t3 = Gson().fromJson(Sudoku.solveString(t2.toString()), Terminal::class.java)

        Assert.assertNotNull(t1)
        Assert.assertNotNull(t3)
        Assert.assertTrue(t1 == t3)
        Assert.assertTrue(t1?.E == 9)
    }

    @Test
    fun test_nullArgument() {
        val signal = CountDownLatch(1)
        var t1: Terminal? = null

        ResolvePuzzleTask(object : PuzzleTask.Callback {
            override fun onResult(result: Terminal?) {
                t1 = result
                signal.countDown()
            }
        }).execute(null)
        signal.await(10, TimeUnit.SECONDS)

        Assert.assertNull(t1)
    }

    @Test
    fun test_illegalArgumentSize() {
        val signal = CountDownLatch(1)
        var t1: Terminal? = null

        ResolvePuzzleTask(object : PuzzleTask.Callback {
            override fun onResult(result: Terminal?) {
                t1 = result
                signal.countDown()
            }
        }).execute()
        signal.await(10, TimeUnit.SECONDS)

        Assert.assertNull(t1)
    }

}
