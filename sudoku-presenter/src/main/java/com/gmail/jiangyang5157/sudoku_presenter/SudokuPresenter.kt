package com.gmail.jiangyang5157.sudoku_presenter

import android.os.AsyncTask
import com.gmail.jiangyang5157.sudoku_presenter.model.Terminal
import com.gmail.jiangyang5157.sudoku_presenter.model.repo.PossibilityRepo
import com.gmail.jiangyang5157.sudoku_presenter.model.repo.PossibilityRepoSpec
import com.gmail.jiangyang5157.sudoku_presenter.model.repo.SudokuRepo
import com.gmail.jiangyang5157.sudoku_presenter.model.repo.SudokuRepoSpec

/**
 * Created by Yang Jiang on April 13, 2018
 */
class SudokuPresenter(view: SudokuContract.View) : SudokuContract.Presenter {

    private val mView = view

    private val mSudokuRepo = SudokuRepo()

    private var mPossibilityRepo: PossibilityRepo? = null

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
                mSudokuRepo.update(result, SudokuRepoSpec(arrayOf(SudokuRepoSpec.INDEX_PUZZLE, SudokuRepoSpec.INDEX_PROGRESS)))
                mSudokuRepo.remove(SudokuRepoSpec(arrayOf(SudokuRepoSpec.INDEX_TERMINAL)))
                mPossibilityRepo = PossibilityRepo(edge)
                mView.showPuzzle(result)
            }
        })
    }

    override fun getTerminal() {
        val terminals = mSudokuRepo.find(SudokuRepoSpec(arrayOf(SudokuRepoSpec.INDEX_TERMINAL)))
        if (terminals.isNotEmpty()) {
            val t = terminals[0]
            mView.showTerminal(t)
        } else {
            val puzzles = mSudokuRepo.find(SudokuRepoSpec(arrayOf(SudokuRepoSpec.INDEX_PUZZLE)))
            if (puzzles.isNotEmpty()) {
                val p = puzzles[0]
                runResolver(p, object : PuzzleTask.Callback {
                    override fun onResult(result: Terminal?) {
                        if (result == null) {
                            return
                        }
                        mSudokuRepo.update(result, SudokuRepoSpec(arrayOf(SudokuRepoSpec.INDEX_TERMINAL)))
                        mView.showTerminal(result)
                    }
                })
            }
        }
    }

    override fun updateProgress(index: Int, d: Int) {
        val progresses = mSudokuRepo.find(SudokuRepoSpec(arrayOf(SudokuRepoSpec.INDEX_PROGRESS)))
        if (progresses.isEmpty()) {
            return
        }
        val p = progresses[0]
        if (index < 0 || index >= p.C.size) {
            return
        }
        p.C[index]?.D = d
        mSudokuRepo.update(p, SudokuRepoSpec(arrayOf(SudokuRepoSpec.INDEX_PROGRESS)))
        mView.showUpdatedProgress(index, d)
    }

    override fun resolveProgress() {
        val progresses = mSudokuRepo.find(SudokuRepoSpec(arrayOf(SudokuRepoSpec.INDEX_PROGRESS)))
        if (progresses.isEmpty()) {
            return
        }
        val p = progresses[0]
        runResolver(p, object : PuzzleTask.Callback {
            override fun onResult(result: Terminal?) {
                if (result == null) {
                    return
                }
                mSudokuRepo.update(result, SudokuRepoSpec(arrayOf(SudokuRepoSpec.INDEX_PROGRESS)))
                mView.showResolvedProgress(result)
            }
        })
    }

    override fun updatePossibility(index: Int, d: Int) {
        val possibilities = mPossibilityRepo?.find(PossibilityRepoSpec(arrayOf(index)))
        if (possibilities == null || possibilities.isEmpty()) {
            return
        }
        val p = possibilities[0]
        val found = p.indexOf(d)
        if (found == -1) {
            for (i in 0 until p.size) {
                if (p[i] == null) {
                    p[i] = d
                    break
                }
            }
        } else {
            p[found] = null
        }
        mPossibilityRepo?.update(p, PossibilityRepoSpec(arrayOf(index)))
        mView.showUpdatedPossibility(index, p)
    }

}
