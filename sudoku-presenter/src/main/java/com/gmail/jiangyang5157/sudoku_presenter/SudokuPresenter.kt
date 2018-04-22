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
class SudokuPresenter(override val mView: SudokuContract.View) : SudokuContract.Presenter {

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

    private var isPossibilityEnterEnable = false

    /**
     * Run Sudoku generator in [GeneratePuzzleTask].
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
     * Run Sudoku resolver in [ResolvePuzzleTask].
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
                } else {
                    mSudokuRepo.update(result, SudokuRepoSpec(intArrayOf(SudokuRepoSpec.INDEX_PUZZLE, SudokuRepoSpec.INDEX_PROGRESS)))
                    mSudokuRepo.remove(SudokuRepoSpec(intArrayOf(SudokuRepoSpec.INDEX_TERMINAL)))
                    mPossibilityRepo = PossibilityRepo(edge)
                    mView.puzzleGenerated(result)
                }
            }
        })
    }

    /**
     * Peek solution 'terminal' of current `puzzle`, then call [SudokuContract.View.terminalReveald].
     * Create solution 'terminal' based on 'puzzle' data if the `terminal` doesn't exist.
     */
    override fun revealTerminal() {
        val terminals = mSudokuRepo.find(SudokuRepoSpec(intArrayOf(SudokuRepoSpec.INDEX_TERMINAL)))
        if (terminals.isEmpty()) {
            val puzzles = mSudokuRepo.find(SudokuRepoSpec(intArrayOf(SudokuRepoSpec.INDEX_PUZZLE)))
            if (puzzles.isEmpty()) {
                mView.terminalReveald(null)
            } else {
                runResolver(puzzles[0], object : PuzzleTask.Callback {
                    override fun onResult(result: Terminal?) {
                        if (result == null) {
                            mView.terminalReveald(null)
                        } else {
                            mSudokuRepo.update(result, SudokuRepoSpec(intArrayOf(SudokuRepoSpec.INDEX_TERMINAL)))
                            mView.terminalReveald(result)
                        }
                    }
                })
            }
        } else {
            mView.terminalReveald(terminals[0])
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
        } else {
            runResolver(progresses[0], object : PuzzleTask.Callback {
                override fun onResult(result: Terminal?) {
                    if (result == null) {
                        mView.progressResolved(null)
                    } else {
                        mSudokuRepo.update(result, SudokuRepoSpec(intArrayOf(SudokuRepoSpec.INDEX_PROGRESS)))
                        mView.progressResolved(result)
                    }
                }
            })
        }

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

        mView.possibilityUpdated(index, possibilities[0])
    }

    /**
     * Select [Cell] at [index], then call [SudokuContract.View.cellSelected] with a [List] of relevant [Cell] indexes
     */
    override fun selectCell(index: Int) {
        val puzzles = mSudokuRepo.find(SudokuRepoSpec(intArrayOf(SudokuRepoSpec.INDEX_PUZZLE)))
        if (puzzles.isEmpty()) {
            mView.cellSelected(index, listOf(index))
        } else {
            mView.cellSelected(index, puzzles[0].relevant(index))
        }
    }

    override fun enablePossibilityEnter() {
        isPossibilityEnterEnable = true
        mView.possibilityEnterEnabled()
    }

    override fun disablePossibilityEnter() {
        isPossibilityEnterEnable = false
        mView.possibilityEnterDisabled()
    }

    override fun invertPossibilityEnterStatus() {
        if (isPossibilityEnterEnable) {
            disablePossibilityEnter()
        } else {
            enablePossibilityEnter()
        }
    }

    override fun enterClear() {
        if (isPossibilityEnterEnable) {
            mView.possibilityCleard()
        } else {
            mView.digitCleard()
        }
    }

    override fun enterDigit(digit: Int) {
        if (isPossibilityEnterEnable) {
            mView.possibilityEnterd(digit)
        } else {
            mView.digitEnterd(digit)
        }
    }

}
