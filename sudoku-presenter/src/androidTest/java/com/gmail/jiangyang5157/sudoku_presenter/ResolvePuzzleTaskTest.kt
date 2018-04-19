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
        val signal = CountDownLatch(2)
        val t1 : Terminal = Gson().fromJson(SudokuSolverTest.TestData.terminalJson_9x9_2, Terminal::class.java)
        var t2: Terminal? = null

        ResolvePuzzleTask(object : ResolvePuzzleTask.PuzzleResolution {

            override fun onPrePuzzleResolution() {
                signal.countDown()
            }

            override fun onPostPuzzleResolution(result: Terminal?) {
                t2 = result
                signal.countDown()
            }
        }).execute(t1)

        signal.await(10, TimeUnit.SECONDS)

        val t3 = Gson().fromJson(Sudoku.solveString(t1.toString()), Terminal::class.java)

        Assert.assertNotNull(t2)
        Assert.assertNotNull(t3)

        Assert.assertTrue(t2 == t3)
        Assert.assertTrue(t2?.E == 9)
    }

}
