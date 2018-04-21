package com.gmail.jiangyang5157.sudoku_presenter

import android.os.AsyncTask
import com.gmail.jiangyang5157.sudoku_presenter.model.Cell
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

    init {
        mView.setPresenter(this)
    }

    /**
     * It holds a [Array] of [Terminal].
     * They are:
     * A non-editable `puzzle` with unique solution;
     * A editable `progress`;
     * A `terminal` indicate the solution of the `puzzle`.
     */
    private val mSudokuRepo = SudokuRepo()

    /**
     * It holds a [Array] of [IntArray] represent possibilities for the [Terminal.C].
     * Each [IntArray] represent possibility for the [Cell].
     */
    private var mPossibilityRepo: PossibilityRepo? = null

    private var mGenerator: GeneratePuzzleTask? = null

    private var mResolver: ResolvePuzzleTask? = null

    private var mBlockMode = 0

    /**
     * Run Sudoku generator in async task.
     */
    private fun runGenerator(blockMode: Int, edge: Int, minSubGiven: Int, minTotalGiven: Int, callback: PuzzleTask.Callback) {
        if (mGenerator?.status != AsyncTask.Status.FINISHED) {
            mGenerator?.cancel(true)
        }
        mGenerator?.mCallback = null
        mGenerator = GeneratePuzzleTask(callback)
        mGenerator?.execute(blockMode, edge, minSubGiven, minTotalGiven)
    }

    /**
     * Run Sudoku resolver in async task.
     */
    private fun runResolver(t: Terminal, callback: PuzzleTask.Callback) {
        if (mResolver?.status != AsyncTask.Status.FINISHED) {
            mResolver?.cancel(true)
        }
        mResolver?.mCallback = null
        mResolver = ResolvePuzzleTask(callback)
        mResolver?.execute(t)
    }

    /**
     * Request a `puzzle`, clear [mPossibilityRepo], then call [SudokuContract.View.puzzleGenerated].
     *
     * It will re-create the 3 [Terminal] data:
     * A new non-editable `puzzle`;
     * A editable `progress` with content equals to the `puzzle`;
     * A lazy `terminal` which will be assigned in [SudokuContract.Presenter.revealTerminal].
     */
    override fun generatePuzzle(edge: Int, minSubGiven: Int, minTotalGiven: Int) {
        runGenerator(mBlockMode, edge, minSubGiven, minTotalGiven, object : PuzzleTask.Callback {
            override fun onResult(result: Terminal?) {
                if (result == null) {
                    mView.puzzleGenerated(null)
                    return
                }
                mSudokuRepo.update(result, SudokuRepoSpec(intArrayOf(SudokuRepoSpec.INDEX_PUZZLE, SudokuRepoSpec.INDEX_PROGRESS)))
                mSudokuRepo.remove(SudokuRepoSpec(intArrayOf(SudokuRepoSpec.INDEX_TERMINAL)))
                mPossibilityRepo = PossibilityRepo(edge)
                mView.puzzleGenerated(result)
            }
        })
    }

    /**
     * Peek solution 'terminal' of current `puzzle`, then call [SudokuContract.View.terminalRevealed].
     * Create solution 'terminal' based on 'puzzle' data if the `terminal` doesn't exist.
     */
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
                            mView.terminalRevealed(null)
                            return
                        }
                        mSudokuRepo.update(result, SudokuRepoSpec(intArrayOf(SudokuRepoSpec.INDEX_TERMINAL)))
                        mView.terminalRevealed(result)
                    }
                })
            }
        }
    }

    /**
     * Update current 'progress', then call [SudokuContract.View.progressUpdated].
     *
     * @throws IllegalStateException
     * @throws IllegalArgumentException
     */
    override fun updateProgress(index: Int, d: Int) {
        val progresses = mSudokuRepo.find(SudokuRepoSpec(intArrayOf(SudokuRepoSpec.INDEX_PROGRESS)))
        if (progresses.isEmpty()) {
            throw IllegalStateException("`progress` not found")
        }
        val p = progresses[0]
        if (index < 0 || index >= p.C.size) {
            throw IllegalArgumentException("[index] out of range")
        }
        p.C[index]?.D = d
        mSudokuRepo.update(p, SudokuRepoSpec(intArrayOf(SudokuRepoSpec.INDEX_PROGRESS)))
        mView.progressUpdated(index, d)
    }

    /**
     * Resolve current 'progress', then call [SudokuContract.View.progressResolved].
     */
    override fun resolveProgress() {
        val progresses = mSudokuRepo.find(SudokuRepoSpec(intArrayOf(SudokuRepoSpec.INDEX_PROGRESS)))
        if (progresses.isEmpty()) {
            mView.progressResolved(null)
            return
        }
        val p = progresses[0]
        runResolver(p, object : PuzzleTask.Callback {
            override fun onResult(result: Terminal?) {
                if (result == null) {
                    mView.progressResolved(null)
                    return
                }
                mSudokuRepo.update(result, SudokuRepoSpec(intArrayOf(SudokuRepoSpec.INDEX_PROGRESS)))
                mView.progressResolved(result)
            }
        })
    }

    /**
     * Update 'possibility' of the [Cell] at [index] with [d], then call [SudokuContract.View.possibilityUpdated].
     *
     * Add the [d] into the 'possibility' if [d] doesn't exist.
     * Remove the [d] from the 'possibility' if [d] already exist.
     *
     * @throws [IllegalStateException]
     */
    override fun updatePossibility(index: Int, d: Int) {
        val possibilities = mPossibilityRepo?.find(PossibilityRepoSpec(intArrayOf(index)))
        if (possibilities == null || possibilities.isEmpty()) {
            throw IllegalStateException("`possibility` not found")
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

    /**
     * Clear 'possibility' of the [Cell] at [index] with default value 0, then call [SudokuContract.View.possibilityUpdated].
     */
    override fun clearPossibility(index: Int) {
        mPossibilityRepo?.remove(PossibilityRepoSpec(intArrayOf(index)))

        val possibilities = mPossibilityRepo?.find(PossibilityRepoSpec(intArrayOf(index)))
        if (possibilities == null || possibilities.isEmpty()) {
            throw IllegalStateException("`possibility` not found")
        }
        val p = possibilities[0]
        mView.possibilityUpdated(index, p)
    }

    /**
     * Select [Cell] at [index], then call [SudokuContract.View.cellSelected] with a [IntArray] of relevant [Cell] indexes
     */
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
