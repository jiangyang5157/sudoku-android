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
        var puzzle: Terminal? = null
        val signal = CountDownLatch(1)

        val view = object : SudokuContract.View {
            var mPresenter: SudokuContract.Presenter

            init {
                mPresenter = SudokuPresenter(this)
            }

            override fun puzzleGenerated(t: Terminal) {
                puzzle = t
                signal.countDown()
            }

            override fun terminalRevealed(t: Terminal) {}

            override fun progressUpdated(index: Int, d: Int) {}

            override fun progressResolved(t: Terminal) {}

            override fun possibilityUpdated(index: Int, possibility: IntArray) {}

            override fun cellSelected(index: Int, relevant: IntArray) {}

            override fun setPresenter(presenter: SudokuContract.Presenter) {
                mPresenter = presenter
            }

        }
        view.mPresenter.start()
        view.mPresenter.generatePuzzle(9, 4, 55)
        signal.await(10, TimeUnit.SECONDS)

        Assert.assertNotNull(puzzle)
    }

    @Test
    fun test_revealTerminal() {
        var puzzle: Terminal? = null
        var terminal: Terminal? = null
        val signal = CountDownLatch(1)

        val view = object : SudokuContract.View {
            var mPresenter: SudokuContract.Presenter

            init {
                mPresenter = SudokuPresenter(this)
            }

            override fun puzzleGenerated(t: Terminal) {
                puzzle = t
                mPresenter.revealTerminal()
            }

            override fun terminalRevealed(t: Terminal) {
                terminal = t
                signal.countDown()
            }

            override fun progressUpdated(index: Int, d: Int) {}

            override fun progressResolved(t: Terminal) {}

            override fun possibilityUpdated(index: Int, possibility: IntArray) {}

            override fun cellSelected(index: Int, relevant: IntArray) {}

            override fun setPresenter(presenter: SudokuContract.Presenter) {
                mPresenter = presenter
            }

        }
        view.mPresenter.start()
        view.mPresenter.generatePuzzle(9, 4, 55)
        signal.await(20, TimeUnit.SECONDS)

        Assert.assertNotNull(puzzle)
        Assert.assertNotNull(terminal)
        Assert.assertTrue(puzzle != terminal)
    }

    @Test
    fun test_runResolver() {
        var puzzle: Terminal? = null
        var resolved: Terminal? = null
        val signal = CountDownLatch(1)

        val view = object : SudokuContract.View {
            var mPresenter: SudokuContract.Presenter

            init {
                mPresenter = SudokuPresenter(this)
            }

            override fun puzzleGenerated(t: Terminal) {
                puzzle = t
                mPresenter.resolveProgress()
            }

            override fun terminalRevealed(t: Terminal) {}

            override fun progressUpdated(index: Int, d: Int) {}

            override fun progressResolved(t: Terminal) {
                resolved = t
                signal.countDown()
            }

            override fun possibilityUpdated(index: Int, possibility: IntArray) {}

            override fun cellSelected(index: Int, relevant: IntArray) {}

            override fun setPresenter(presenter: SudokuContract.Presenter) {
                mPresenter = presenter
            }

        }
        view.mPresenter.start()
        view.mPresenter.generatePuzzle(9, 4, 55)
        signal.await(20, TimeUnit.SECONDS)

        Assert.assertNotNull(puzzle)
        Assert.assertNotNull(resolved)
        Assert.assertTrue(puzzle != resolved)
    }

    @Test
    fun test_updateProgress() {
        var puzzle: Terminal? = null
        var indexResult = 0
        var dResult = 0
        val signal = CountDownLatch(1)

        val view = object : SudokuContract.View {
            var mPresenter: SudokuContract.Presenter

            init {
                mPresenter = SudokuPresenter(this)
            }

            override fun puzzleGenerated(t: Terminal) {
                puzzle = t
                mPresenter.updateProgress(1, 2)
            }

            override fun terminalRevealed(t: Terminal) {}

            override fun progressUpdated(index: Int, d: Int) {
                indexResult = index
                dResult = d
                signal.countDown()
            }

            override fun progressResolved(t: Terminal) {}

            override fun possibilityUpdated(index: Int, possibility: IntArray) {}

            override fun cellSelected(index: Int, relevant: IntArray) {}

            override fun setPresenter(presenter: SudokuContract.Presenter) {
                mPresenter = presenter
            }

        }
        view.mPresenter.start()
        view.mPresenter.generatePuzzle(9, 4, 55)
        signal.await(10, TimeUnit.SECONDS)

        Assert.assertNotNull(puzzle)
        Assert.assertEquals(1, indexResult)
        Assert.assertEquals(2, dResult)
    }

    @Test
    fun test_updatePossibility() {
        var puzzle: Terminal? = null
        var indexResult = 0
        var possibilityResult = IntArray(9)
        val signal = CountDownLatch(7)

        val view = object : SudokuContract.View {
            var mPresenter: SudokuContract.Presenter

            init {
                mPresenter = SudokuPresenter(this)
            }

            override fun puzzleGenerated(t: Terminal) {
                puzzle = t
                mPresenter.updatePossibility(1, 2)
                mPresenter.updatePossibility(1, 3)
                mPresenter.updatePossibility(1, 4)
                mPresenter.updatePossibility(1, 5)
                mPresenter.updatePossibility(1, 3)
                mPresenter.updatePossibility(1, 4)
                mPresenter.updatePossibility(1, 4)
            }

            override fun terminalRevealed(t: Terminal) {}

            override fun progressUpdated(index: Int, d: Int) {}

            override fun progressResolved(t: Terminal) {}

            override fun possibilityUpdated(index: Int, possibility: IntArray) {
                indexResult = index
                possibilityResult = possibility
                signal.countDown()
            }

            override fun cellSelected(index: Int, relevant: IntArray) {}

            override fun setPresenter(presenter: SudokuContract.Presenter) {
                mPresenter = presenter
            }

        }
        view.mPresenter.start()
        view.mPresenter.generatePuzzle(9, 4, 55)
        signal.await(10, TimeUnit.SECONDS)

        Assert.assertNotNull(puzzle)
        Assert.assertEquals(1, indexResult)
        Assert.assertTrue("Updates: 2,3,4,5,3,4,4 Actual:" + Arrays.toString(possibilityResult), intArrayOf(2, 4, 0, 5, 0, 0, 0, 0, 0).contentEquals(possibilityResult))
    }

    @Test
    fun test_clearPossibility() {
        var puzzle: Terminal? = null
        var indexResult = 0
        var possibilityResult = IntArray(9)
        val signal = CountDownLatch(7)

        val view = object : SudokuContract.View {
            var mPresenter: SudokuContract.Presenter

            init {
                mPresenter = SudokuPresenter(this)
            }

            override fun puzzleGenerated(t: Terminal) {
                puzzle = t

                mPresenter.updatePossibility(1, 2)
                mPresenter.updatePossibility(1, 3)
                mPresenter.updatePossibility(1, 4)
                mPresenter.updatePossibility(1, 5)
                mPresenter.updatePossibility(1, 3)
                mPresenter.updatePossibility(1, 4)
                mPresenter.clearPossibility(1)
            }

            override fun terminalRevealed(t: Terminal) {}

            override fun progressUpdated(index: Int, d: Int) {}

            override fun progressResolved(t: Terminal) {}

            override fun possibilityUpdated(index: Int, possibility: IntArray) {
                indexResult = index
                possibilityResult = possibility
                signal.countDown()
            }

            override fun cellSelected(index: Int, relevant: IntArray) {}

            override fun setPresenter(presenter: SudokuContract.Presenter) {
                mPresenter = presenter
            }

        }
        view.mPresenter.start()
        view.mPresenter.generatePuzzle(9, 4, 55)
        signal.await(10, TimeUnit.SECONDS)

        Assert.assertNotNull(puzzle)
        Assert.assertEquals(1, indexResult)
        Assert.assertTrue("Actual:" + Arrays.toString(possibilityResult),
                intArrayOf(0, 0, 0, 0, 0, 0, 0, 0, 0).contentEquals(possibilityResult))
    }

    @Test
    fun test_selectCell() {
        var puzzle: Terminal? = null
        var indexResult = 0
        var relevantResult = IntArray(9)
        val signal = CountDownLatch(1)

        val view = object : SudokuContract.View {
            var mPresenter: SudokuContract.Presenter

            init {
                mPresenter = SudokuPresenter(this)
            }

            override fun puzzleGenerated(t: Terminal) {
                puzzle = t
                mPresenter.selectCell(41)
            }

            override fun terminalRevealed(t: Terminal) {}

            override fun progressUpdated(index: Int, d: Int) {}

            override fun progressResolved(t: Terminal) {}

            override fun possibilityUpdated(index: Int, possibility: IntArray) {}

            override fun cellSelected(index: Int, relevant: IntArray) {
                indexResult = index
                relevantResult = relevant
                signal.countDown()
            }

            override fun setPresenter(presenter: SudokuContract.Presenter) {
                mPresenter = presenter
            }

        }
        view.mPresenter.start()
        view.mPresenter.generatePuzzle(9, 4, 55)
        signal.await(10, TimeUnit.SECONDS)

        Assert.assertNotNull(puzzle)
        Assert.assertEquals(41, indexResult)
//                                5,
//                                14,
//                                23,
//                        30, 31, 32,
//            36, 37, 38, 39, 40, 41, 42, 43, 44,
//                        48, 49, 50,
//                                59,
//                                68,
//                                77
        Assert.assertTrue("Actual:" + Arrays.toString(relevantResult),
                intArrayOf(
                        5,
                        14,
                        23,
                        30, 31, 32,
                        36, 37, 38, 39, 40, 41, 42, 43, 44,
                        48, 49, 50,
                        59,
                        68,
                        77
                ).contentEquals(relevantResult))
    }

}