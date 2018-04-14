package com.gmail.jiangyang5157.sudoku_presenter

import android.os.AsyncTask
import com.gmail.jiangyang5157.sudoku_presenter.model.Terminal
import sudoku.Sudoku

/**
 * Created by Yang Jiang on April 14, 2018
 *
 * Execute Terminal arguments: puzzle to be resolved
 * Result Terminal puzzle resolution
 */
class ResolvePuzzleTask(puzzleResolution: SudokuContract.PuzzleResolution) : AsyncTask<Terminal, Void, Terminal>() {

    private val mPuzzleResolution: SudokuContract.PuzzleResolution = puzzleResolution

    override fun onPreExecute() {
        mPuzzleResolution.onPrePuzzleResolution()
    }

    override fun doInBackground(vararg params: Terminal?): Terminal {
        val tIn = params[0]!!

        // todo convert Terminal 2 String
        val sIn = ""
        val sOut = Sudoku.solveString(sIn)
        // todo convert String 2 Terminal
        val tOut = Terminal(tIn.e)

        return tOut
    }

    override fun onCancelled(result: Terminal?) {
        mPuzzleResolution.onPostPuzzleResolution(result)
    }

}