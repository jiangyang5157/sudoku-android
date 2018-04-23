package com.gmail.jiangyang5157.sudoku_presenter

import android.support.test.runner.AndroidJUnit4
import com.gmail.jiangyang5157.sudoku_presenter.model.Terminal
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import java.util.*
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

/**
 * Created by Yang Jiang on April 20, 2018
 */
@RunWith(AndroidJUnit4::class)
class SudokuPresenterTest {

    @Test
    fun test_generatePuzzle() {
        var mPuzzle: Terminal? = null
        val signal = CountDownLatch(1)

        val view = object : SudokuContract.View {

            lateinit var mPresenter: SudokuContract.Presenter

            override fun puzzleGenerated(puzzle: Terminal?) {
                mPuzzle = puzzle
                signal.countDown()
            }

            override fun terminalReveald(terminal: Terminal?) {}

            override fun progressUpdated(index: Int, digit: Int) {}

            override fun progressCleard(progress: Terminal) {}

            override fun possibilityUpdated(index: Int, possibility: Array<Int?>) {}

            override fun setPresenter(presenter: SudokuContract.Presenter) {
                mPresenter = presenter
            }

        }
        val presenter = SudokuPresenter(view)
        presenter.generatePuzzle(9, 4, 55)
        signal.await(10, TimeUnit.SECONDS)

        Assert.assertNotNull(mPuzzle)
    }

    @Test
    fun test_revealTerminal() {
        var mPuzzle: Terminal? = null
        var mTerminal: Terminal? = null
        val signal = CountDownLatch(1)

        val view = object : SudokuContract.View {
            lateinit var mPresenter: SudokuContract.Presenter

            override fun puzzleGenerated(puzzle: Terminal?) {
                mPuzzle = puzzle
                mPresenter.revealTerminal()
            }

            override fun terminalReveald(terminal: Terminal?) {
                mTerminal = terminal
                signal.countDown()
            }

            override fun progressUpdated(index: Int, digit: Int) {}

            override fun progressCleard(progress: Terminal) {}

            override fun possibilityUpdated(index: Int, possibility: Array<Int?>) {}

            override fun setPresenter(presenter: SudokuContract.Presenter) {
                mPresenter = presenter
            }

        }
        val presenter = SudokuPresenter(view)
        presenter.generatePuzzle(9, 4, 55)
        signal.await(20, TimeUnit.SECONDS)

        Assert.assertNotNull(mPuzzle)
        Assert.assertNotNull(mTerminal)
        Assert.assertTrue(mPuzzle != mTerminal)
    }

    @Test
    fun test_updateProgress() {
        var mPuzzle: Terminal? = null
        var indexResult = 0
        var dResult = 0
        val signal = CountDownLatch(1)

        val view = object : SudokuContract.View {

            lateinit var mPresenter: SudokuContract.Presenter

            override fun puzzleGenerated(puzzle: Terminal?) {
                mPuzzle = puzzle
                mPresenter.updateProgress(1, 2)
            }

            override fun terminalReveald(terminal: Terminal?) {}

            override fun progressUpdated(index: Int, digit: Int) {
                indexResult = index
                dResult = digit
                signal.countDown()
            }

            override fun progressCleard(progress: Terminal) {}

            override fun possibilityUpdated(index: Int, possibility: Array<Int?>) {}

            override fun setPresenter(presenter: SudokuContract.Presenter) {
                mPresenter = presenter
            }

        }
        val presenter = SudokuPresenter(view)
        presenter.generatePuzzle(9, 4, 55)
        signal.await(10, TimeUnit.SECONDS)

        Assert.assertNotNull(mPuzzle)
        Assert.assertEquals(1, indexResult)
        Assert.assertEquals(2, dResult)
    }

    @Test
    fun test_updatePossibility() {
        var mPuzzle: Terminal? = null
        var indexResult = 0
        var possibilityResult = arrayOfNulls<Int>(9)
        val signal = CountDownLatch(7)

        val view = object : SudokuContract.View {
            lateinit var mPresenter: SudokuContract.Presenter

            override fun puzzleGenerated(puzzle: Terminal?) {
                mPuzzle = puzzle
                mPresenter.updatePossibility(1, 2)
                mPresenter.updatePossibility(1, 3)
                mPresenter.updatePossibility(1, 4)
                mPresenter.updatePossibility(1, 5)
                mPresenter.updatePossibility(1, 3)
                mPresenter.updatePossibility(1, 4)
                mPresenter.updatePossibility(1, 4)
            }

            override fun terminalReveald(terminal: Terminal?) {}

            override fun progressUpdated(index: Int, digit: Int) {}

            override fun progressCleard(progress: Terminal) {}

            override fun possibilityUpdated(index: Int, possibility: Array<Int?>) {
                indexResult = index
                possibilityResult = possibility
                signal.countDown()
            }

            override fun setPresenter(presenter: SudokuContract.Presenter) {
                mPresenter = presenter
            }

        }
        val presenter = SudokuPresenter(view)
        presenter.generatePuzzle(9, 4, 55)
        signal.await(10, TimeUnit.SECONDS)

        Assert.assertNotNull(mPuzzle)
        Assert.assertEquals(1, indexResult)
        Assert.assertTrue("Updates: 2,3,4,5,3,4,4 Actual:" + Arrays.toString(possibilityResult),
                arrayOf(2, 4, null, 5, null, null, null, null, null).contentEquals(possibilityResult))
    }

    @Test
    fun test_clearPossibility() {
        var mPuzzle: Terminal? = null
        var indexResult = 0
        var possibilityResult = arrayOfNulls<Int>(9)
        val signal = CountDownLatch(7)

        val view = object : SudokuContract.View {
            lateinit var mPresenter: SudokuContract.Presenter

            override fun puzzleGenerated(puzzle: Terminal?) {
                mPuzzle = puzzle

                mPresenter.updatePossibility(1, 2)
                mPresenter.updatePossibility(1, 3)
                mPresenter.updatePossibility(1, 4)
                mPresenter.updatePossibility(1, 5)
                mPresenter.updatePossibility(1, 3)
                mPresenter.updatePossibility(1, 4)
                mPresenter.clearPossibility(1)
            }

            override fun terminalReveald(terminal: Terminal?) {}

            override fun progressUpdated(index: Int, digit: Int) {}

            override fun progressCleard(progress: Terminal) {}

            override fun possibilityUpdated(index: Int, possibility: Array<Int?>) {
                indexResult = index
                possibilityResult = possibility
                signal.countDown()
            }

            override fun setPresenter(presenter: SudokuContract.Presenter) {
                mPresenter = presenter
            }

        }
        val presenter = SudokuPresenter(view)
        presenter.generatePuzzle(9, 4, 55)
        signal.await(10, TimeUnit.SECONDS)

        Assert.assertNotNull(mPuzzle)
        Assert.assertEquals(1, indexResult)
        Assert.assertTrue("Actual:" + Arrays.toString(possibilityResult),
                arrayOfNulls<Int?>(9).contentEquals((possibilityResult)))
    }

}