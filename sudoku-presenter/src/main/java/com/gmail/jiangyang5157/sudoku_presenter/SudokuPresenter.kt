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

    companion object {
        const val BLOCK_MODE = 0
    }

    private val mView = view

    private val mSudokuRepo = SudokuRepo()

    private var mPossibilityRepo: PossibilityRepo? = null

    private var mGenerator: GeneratePuzzleTask? = null

    private var mResolver: ResolvePuzzleTask? = null

    private var mBlockMode = BLOCK_MODE

    private fun runGenerator(blockMode: Int, edge: Int, minSubGiven: Int, minTotalGiven: Int, callback: PuzzleTask.Callback) {
        if (mGenerator?.status != AsyncTask.Status.FINISHED) {
            mGenerator?.cancel(true)
        }
        mGenerator?.mCallback = null
        mGenerator = GeneratePuzzleTask(callback)
        mGenerator?.execute(blockMode, edge, minSubGiven, minTotalGiven)
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

    override fun generatePuzzle(edge: Int, minSubGiven: Int, minTotalGiven: Int) {
        runGenerator(mBlockMode, edge, minSubGiven, minTotalGiven, object : PuzzleTask.Callback {
            override fun onResult(result: Terminal?) {
                if (result == null) {
                    return
                }
                mSudokuRepo.update(result, SudokuRepoSpec(intArrayOf(SudokuRepoSpec.INDEX_PUZZLE, SudokuRepoSpec.INDEX_PROGRESS)))
                mSudokuRepo.remove(SudokuRepoSpec(intArrayOf(SudokuRepoSpec.INDEX_TERMINAL)))
                mPossibilityRepo = PossibilityRepo(edge)
                mView.puzzleGenerated(result)
            }
        })
    }

    override fun revealTerminal() {
        val terminals = mSudokuRepo.find(SudokuRepoSpec(intArrayOf(SudokuRepoSpec.INDEX_TERMINAL)))
        if (terminals.isNotEmpty()) {
            val t = terminals[0]
            mView.terminalRevealed(t)
        } else {
            val puzzles = mSudokuRepo.find(SudokuRepoSpec(intArrayOf(SudokuRepoSpec.INDEX_PUZZLE)))
            if (puzzles.isNotEmpty()) {
                val p = puzzles[0]
                runResolver(p, object : PuzzleTask.Callback {
                    override fun onResult(result: Terminal?) {
                        if (result == null) {
                            return
                        }
                        mSudokuRepo.update(result, SudokuRepoSpec(intArrayOf(SudokuRepoSpec.INDEX_TERMINAL)))
                        mView.terminalRevealed(result)
                    }
                })
            }
        }
    }

    override fun updateProgress(index: Int, d: Int) {
        val progresses = mSudokuRepo.find(SudokuRepoSpec(intArrayOf(SudokuRepoSpec.INDEX_PROGRESS)))
        if (progresses.isEmpty()) {
            return
        }
        val p = progresses[0]
        if (index < 0 || index >= p.C.size) {
            return
        }
        p.C[index]?.D = d
        mSudokuRepo.update(p, SudokuRepoSpec(intArrayOf(SudokuRepoSpec.INDEX_PROGRESS)))
        mView.progressUpdated(index, d)
    }

    override fun resolveProgress() {
        val progresses = mSudokuRepo.find(SudokuRepoSpec(intArrayOf(SudokuRepoSpec.INDEX_PROGRESS)))
        if (progresses.isEmpty()) {
            return
        }
        val p = progresses[0]
        runResolver(p, object : PuzzleTask.Callback {
            override fun onResult(result: Terminal?) {
                if (result == null) {
                    return
                }
                mSudokuRepo.update(result, SudokuRepoSpec(intArrayOf(SudokuRepoSpec.INDEX_PROGRESS)))
                mView.progressResolved(result)
            }
        })
    }

    override fun updatePossibility(index: Int, d: Int) {
        val possibilities = mPossibilityRepo?.find(PossibilityRepoSpec(intArrayOf(index)))
        if (possibilities == null || possibilities.isEmpty()) {
            return
        }
        val p = possibilities[0]
        val found = p.indexOfFirst { it == d }
        if (found >= 0) {
            p[found] = 0
        } else {
            val i = p.indexOfFirst { it == 0 }
            if (i >= 0) {
                p[i] = d
            }
        }
        mPossibilityRepo?.update(p, PossibilityRepoSpec(intArrayOf(index)))
        mView.possibilityUpdated(index, p)
    }

    override fun clearPossibility(index: Int) {
        mPossibilityRepo?.remove(PossibilityRepoSpec(intArrayOf(index)))

        val possibilities = mPossibilityRepo?.find(PossibilityRepoSpec(intArrayOf(index)))
        if (possibilities == null || possibilities.isEmpty()) {
            return
        }
        val p = possibilities[0]
        mView.possibilityUpdated(index, p)
    }

    override fun selectCell(index: Int) {
        val ret: MutableSet<Int> = mutableSetOf()
        val puzzles = mSudokuRepo.find(SudokuRepoSpec(intArrayOf(SudokuRepoSpec.INDEX_PUZZLE)))
        if (puzzles.isNotEmpty()) {
            val p = puzzles[0]
            val b = p.C[index]?.B
            val row = Terminal.row(p.E, index)
            val col = Terminal.col(p.E, index)
            p.C.forEachIndexed { index2, cell ->
                if (cell?.B == b || Terminal.row(p.E, index2) == row || Terminal.col(p.E, index2) == col) {
                    ret.add(index2)
                }
            }
        }
        mView.cellSelected(index, ret.toIntArray())
    }

}
