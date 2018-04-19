package com.gmail.jiangyang5157.sudoku_presenter

import android.os.AsyncTask
import com.gmail.jiangyang5157.sudoku_presenter.model.Terminal
import com.google.gson.Gson
import sudoku.Sudoku

/**
 * Created by Yang Jiang on April 14, 2018
 */
class ResolvePuzzleTask(callback: Callback) : AsyncTask<Terminal?, Void, Terminal?>() {

    interface Callback {
        fun onResolved(result: Terminal?)
    }

    private val mCallback: Callback = callback

    override fun doInBackground(vararg params: Terminal?): Terminal? {
        if (params.size < 1) {
            return null
        }
        val s =
                params[0]?.toString()?.let { puzzle ->
                    Sudoku.solveString(puzzle)
                }

        return Gson().fromJson(s, Terminal::class.java)
    }

    override fun onPostExecute(result: Terminal?) {
        mCallback.onResolved(result)
    }

    override fun onCancelled() {
        mCallback.onResolved(null)
    }

}