package com.gmail.jiangyang5157.sudoku_presenter

import android.support.test.runner.AndroidJUnit4
import com.gmail.jiangyang5157.sudoku_presenter.model.Terminal
import org.junit.Assert

import org.junit.Test
import org.junit.runner.RunWith
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

/**
 * Created by Yang Jiang on April 14, 2018
 */
@RunWith(AndroidJUnit4::class)
class GeneratePuzzleTaskTest {

    @Test
    fun test_onGenerated() {
        var t: Terminal? = null
        val signal = CountDownLatch(1)

        GeneratePuzzleTask(object : PuzzleTask.Callback {
            override fun onResult(result: Terminal?) {
                t = result
                signal.countDown()
            }
        }).execute(0, 9, 4, 55)
        signal.await(10, TimeUnit.SECONDS)

        Assert.assertNotNull(t)
    }

    @Test
    fun test_nullArgument() {
        var t: Terminal? = null
        val signal = CountDownLatch(1)

        GeneratePuzzleTask(object : PuzzleTask.Callback {
            override fun onResult(result: Terminal?) {
                t = result
                signal.countDown()
            }
        }).execute(0, 9, 4, null)
        signal.await(10, TimeUnit.SECONDS)

        Assert.assertNull(t)
    }

    @Test
    fun test_illegalArgumentSize() {
        var t: Terminal? = null
        val signal = CountDownLatch(1)

        GeneratePuzzleTask(object : PuzzleTask.Callback {
            override fun onResult(result: Terminal?) {
                t = result
                signal.countDown()
            }
        }).execute(0, 9, 4)
        signal.await(10, TimeUnit.SECONDS)

        Assert.assertNull(t)
    }

}
