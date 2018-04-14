package com.gmail.jiangyang5157.sudoku_presenter

import android.support.test.runner.AndroidJUnit4
import com.gmail.jiangyang5157.sudoku_presenter.model.Terminal
import org.junit.Assert

import org.junit.Test;
import org.junit.runner.RunWith;
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

/**
 * Created by Yang Jiang on April 14, 2018
 */
@RunWith(AndroidJUnit4::class)
class GeneratePuzzleTaskTest {

    @Test
    fun test_execute() {
        val signal = CountDownLatch(2)
        var t : Terminal? = null

        GeneratePuzzleTask(object : SudokuContract.PuzzleGeneration {

            override fun onPrePuzzleGeneration() {
                signal.countDown()
            }

            override fun onPostPuzzleGeneration(result: Terminal?) {
                t = result
                signal.countDown()
            }
        }).execute(9, 4, 55)

        signal.await(10, TimeUnit.SECONDS)
        Assert.assertNotNull(t)
    }

}
