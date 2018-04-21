package com.gmail.jiangyang5157.sudoku_presenter

import android.os.AsyncTask
import com.gmail.jiangyang5157.sudoku_presenter.model.Terminal

/**
 * Created by Yang Jiang on April 19, 2018
 *
 * Simplified AsyncTask to handle Sudoku async task
 */
abstract class PuzzleTask<Params, Progress, Result>
    : AsyncTask<Params, Progress, Result>() {

    interface Callback {
        fun onResult(result: Terminal?)
    }

    var mCallback: Callback? = null

}