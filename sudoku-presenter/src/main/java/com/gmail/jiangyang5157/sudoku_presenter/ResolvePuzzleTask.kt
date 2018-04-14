package com.gmail.jiangyang5157.sudoku_presenter

import android.os.AsyncTask
import com.gmail.jiangyang5157.sudoku_presenter.model.Terminal
import com.google.gson.Gson
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
        val t = params[0]!!

        val s = Sudoku.solveString(t.toString())
        return Gson().fromJson(s, Terminal::class.java)
    }

    override fun onPostExecute(result: Terminal?) {
        mPuzzleResolution.onPostPuzzleResolution(result)
    }

    override fun onCancelled(result: Terminal?) {
        mPuzzleResolution.onPostPuzzleResolution(result)
    }

}