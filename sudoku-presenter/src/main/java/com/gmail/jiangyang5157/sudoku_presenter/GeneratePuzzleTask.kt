package com.gmail.jiangyang5157.sudoku_presenter

import com.gmail.jiangyang5157.sudoku_presenter.model.Terminal
import com.google.gson.Gson
import sudoku.Sudoku

/**
 * Created by Yang Jiang on April 14, 2018
 */
class GeneratePuzzleTask(callback: Callback? = null) : PuzzleTask<Int?, Void, Terminal?>() {

    init {
        mCallback = callback
    }

    override fun doInBackground(vararg params: Int?): Terminal? {
        if (params.isEmpty() || params.size < 3) {
            return null
        }
        val s =
                params[0]?.toLong()?.let { edge ->
                    params[1]?.toLong()?.let { minSubGiven ->
                        params[2]?.toLong()?.let { minTotalGiven ->
                            Sudoku.genString(0, edge, minSubGiven, minTotalGiven)
                        }
                    }
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