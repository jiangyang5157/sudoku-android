package com.gmail.jiangyang5157.sudoku_presenter

import com.gmail.jiangyang5157.sudoku_presenter.model.Terminal
import com.google.gson.Gson
import sudoku.Sudoku

/**
 * Created by Yang Jiang on April 14, 2018
 *
 * [Sudoku.solveString]
 * https://github.com/jiangyang5157/go-sudoku/blob/master/sudoku/solver.go
 * https://github.com/jiangyang5157/go-dlx/blob/master/dlx/dlx.go
 */
class ResolvePuzzleTask(callback: Callback? = null) : PuzzleTask<Terminal?, Void, Terminal?>() {

    init {
        mCallback = callback
    }

    override fun doInBackground(vararg params: Terminal?): Terminal? {
        if (params.isEmpty()) {
            return null
        }
        val s =
                params[0]?.toString()?.let { puzzle ->
                    Sudoku.solveString(puzzle)
                }

        return Gson().fromJson(s, Terminal::class.java)
    }

    override fun onPostExecute(result: Terminal?) {
        mCallback?.onResult(result)
    }

    override fun onCancelled() {
        mCallback?.onResult(null)
    }

}