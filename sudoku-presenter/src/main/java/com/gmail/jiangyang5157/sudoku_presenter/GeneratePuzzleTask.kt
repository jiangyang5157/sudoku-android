package com.gmail.jiangyang5157.sudoku_presenter

import com.gmail.jiangyang5157.sudoku_presenter.model.Terminal
import com.google.gson.Gson
import sudoku.Sudoku

/**
 * Created by Yang Jiang on April 14, 2018
 *
 * Async task to handle puzzle generation.
 *
 * @param blockMode Always passing `SQUARE(0)`, since `IRREGULAR(1)` has not yet implemented
 * @param edge
 * @param minSubGiven
 * @param minTotalGiven
 */
class GeneratePuzzleTask(callback: Callback? = null) : PuzzleTask<Int?, Void, Terminal?>() {

    init {
        mCallback = callback
    }

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