package com.gmail.jiangyang5157.sudoku_presenter

import com.gmail.jiangyang5157.sudoku_presenter.model.Terminal
import com.google.gson.Gson
import sudoku.Sudoku

/**
 * Created by Yang Jiang on April 14, 2018
 *
 * Async task to handle puzzle generation.
 */
class GeneratePuzzleTask(callback: Callback? = null) : PuzzleTask<Int?, Void, Terminal?>() {

    init {
        mCallback = callback
    }

    /**
     * @param params[0] `blockMode`
     * @param params[1] `edge`
     * @param params[2] `minSubGiven`
     * @param params[3] `minTotalGiven`
     */
    override fun doInBackground(vararg params: Int?): Terminal? {
        if (params.isEmpty() || params.size < 4) {
            return null
        }
        val s =
                params[0]?.toLong()?.let { blockMode ->
                    params[1]?.toLong()?.let { edge ->
                        params[2]?.toLong()?.let { minSubGiven ->
                            params[3]?.toLong()?.let { minTotalGiven ->
                                Sudoku.genString(blockMode, edge, minSubGiven, minTotalGiven)
                            }
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