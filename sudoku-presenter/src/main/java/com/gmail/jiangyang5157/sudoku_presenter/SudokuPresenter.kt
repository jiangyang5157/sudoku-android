package com.gmail.jiangyang5157.sudoku_presenter

import android.os.AsyncTask
import com.gmail.jiangyang5157.sudoku_presenter.model.Cell
import com.gmail.jiangyang5157.sudoku_presenter.model.Sudoku
import com.gmail.jiangyang5157.sudoku_presenter.model.Terminal

/**
 * Created by Yang Jiang on April 13, 2018
 */
class SudokuPresenter(override val mView: SudokuContract.View) : SudokuContract.Presenter {

    init {
        mView.setPresenter(this)
    }

    private var mGenerator: GeneratePuzzleTask? = null

    private var mResolver: ResolvePuzzleTask? = null

    private var mSudoku: Sudoku? = null

    // Use `SQUARE(0)`, since `IRREGULAR(1)` has not yet implemented
    private var mBlockMode = 0

    private fun runGenerator(blockMode: Int, edge: Int, minSubGiven: Int, minTotalGiven: Int, callback: PuzzleTask.Callback) {
        mGenerator?.apply {
            if (status != AsyncTask.Status.FINISHED) {
                cancel(true)
            }
            mCallback = null
        }
        mGenerator = GeneratePuzzleTask(callback).apply { execute(blockMode, edge, minSubGiven, minTotalGiven) }
    }

    private fun runResolver(t: Terminal, callback: PuzzleTask.Callback) {
        mResolver?.apply {
            if (status != AsyncTask.Status.FINISHED) {
                cancel(true)
            }
            mCallback = null
        }
        mResolver = ResolvePuzzleTask(callback).apply { execute(t) }
    }

    override fun generatePuzzle(edge: Int, minSubGiven: Int, minTotalGiven: Int) {
        if (edge <= 0 || minSubGiven < 0 || minTotalGiven < 0) {
            throw IllegalArgumentException()
        }

        runGenerator(mBlockMode, edge, minSubGiven, minTotalGiven, object : PuzzleTask.Callback {
            override fun onResult(result: Terminal?) {
                result?.apply {
                    mSudoku = Sudoku(this)
                    mView.puzzleGenerated(this)
                }
            }
        })
    }

    override fun revealTerminal() {
        mSudoku?.apply {
            runResolver(T, object : PuzzleTask.Callback {
                override fun onResult(result: Terminal?) {
                    result?.apply {
                        mView.terminalReveald(result)
                    }
                }
            })
        }
    }

    override fun updateProgress(index: Int, digit: Int) {
        if (index < 0 || digit < 0) {
            throw IllegalArgumentException()
        }

        if (isCellEditable(index)) {
            mSudoku?.PT?.T?.apply {
                C[index]?.apply {
                    C[index] = Cell(B, digit)
                    mView.progressUpdated(index, digit)
                }
            }
        }
    }

    override fun clearProgress() {
        mSudoku?.apply {
            mView.progressCleard(
                    Sudoku(T).apply {
                        mSudoku = this
                    }.PT.T
            )
        }
    }

    override fun updatePossibility(index: Int, digit: Int) {
        if (index < 0 || digit < 0) {
            throw IllegalArgumentException()
        }

        if (isCellEditable(index)) {
            mSudoku?.PT?.apply {
                val found = P[index].indexOfFirst { it == digit }
                if (found >= 0) {
                    P[index][found] = null
                } else {
                    val i = P[index].indexOfFirst { it == null }
                    if (i >= 0) {
                        P[index][i] = digit
                    }
                }
                mView.possibilityUpdated(index, P[index])
            }
        }
    }

    override fun clearPossibility(index: Int) {
        if (index < 0) {
            throw IllegalArgumentException()
        }

        if (isCellEditable(index)) {
            mSudoku?.PT?.apply {
                P[index] = arrayOfNulls(T.E)
                mView.possibilityUpdated(index, P[index])
            }
        }
    }

    private fun isCellEditable(index: Int): Boolean {
        mSudoku?.apply {
            if (T.C[index]?.D == 0) {
                return true
            }
        }
        return false
    }

}
