package com.gmail.jiangyang5157.sudoku_presenter

import android.os.AsyncTask
import com.gmail.jiangyang5157.sudoku_presenter.model.Terminal
import com.gmail.jiangyang5157.sudoku_presenter.model.repo.*

/**
 * Created by Yang Jiang on April 13, 2018
 */
class SudokuPresenter(view: SudokuContract.View) : SudokuContract.Presenter {

    private val mView = view

    private val mRepo = SudokuRepo()

    private var mGenerator: GeneratePuzzleTask? = null

    private var mResolver: ResolvePuzzleTask? = null

    private fun runGenerator(edge: Int, minSubGiven: Int, minTotalGiven: Int, callback: PuzzleTask.Callback) {
        if (mGenerator?.status != AsyncTask.Status.FINISHED) {
            mGenerator?.cancel(true)
        }
        mGenerator?.mCallback = null
        mGenerator = GeneratePuzzleTask(callback)
        mGenerator?.execute(edge, minSubGiven, minTotalGiven)
    }

    private fun runResolver(t: Terminal, callback: PuzzleTask.Callback) {
        if (mResolver?.status != AsyncTask.Status.FINISHED) {
            mResolver?.cancel(true)
        }
        mResolver?.mCallback = null
        mResolver = ResolvePuzzleTask(callback)
        mResolver?.execute(t)
    }

    override fun start() {
        mView.setPresenter(this)
    }

    override fun getPuzzle(edge: Int, minSubGiven: Int, minTotalGiven: Int) {
        runGenerator(edge, minSubGiven, minTotalGiven, object : PuzzleTask.Callback {
            override fun onResult(result: Terminal?) {
                if (result == null) {
                    return
                }
                mRepo.update(result, SudokuFilter())
                mRepo.remove(TerminalFilter())
                mView.showPuzzle(result)
            }
        })
    }

    override fun getTerminal() {
        val terminals = mRepo.find(TerminalFilter())
        if (terminals.isNotEmpty()) {
            val t = terminals[0]
            mView.showTerminal(t)
        } else {
            val puzzles = mRepo.find(PuzzleFilter())
            if (puzzles.isNotEmpty()) {
                val p = puzzles[0]
                runResolver(p, object : PuzzleTask.Callback {
                    override fun onResult(result: Terminal?) {
                        if (result == null) {
                            return
                        }
                        mRepo.update(result, TerminalFilter())
                        mView.showTerminal(result)
                    }
                })
            }
        }
    }

    override fun updateProgress(index: Int, d: Int) {
        val progresses = mRepo.find(ProgressFilter())
        if (progresses.isEmpty()) {
            return
        }
        val p = progresses[0]
        if (index < 0 || index >= p.C.size) {
            return
        }
        p.C[index]?.D = d
        mRepo.update(p, ProgressFilter())
        mView.showUpdatedProgress(p)
    }

    override fun resolveProgress() {
        val progresses = mRepo.find(ProgressFilter())
        if (progresses.isEmpty()) {
            return
        }
        val p = progresses[0]
        runResolver(p, object : PuzzleTask.Callback {
            override fun onResult(result: Terminal?) {
                if (result == null) {
                    return
                }
                mRepo.update(result, ProgressFilter())
                mView.showResolvedProgress(result)
            }
        })
    }

}
