package com.gmail.jiangyang5157.sudoku_presenter

import android.os.AsyncTask
import com.gmail.jiangyang5157.sudoku_presenter.model.Terminal
import com.google.gson.Gson
import sudoku.Sudoku

/**
 * Created by Yang Jiang on April 14, 2018
 *
 * Execute Int arguments: edge, minSubGiven, minTotalGiven
 * Result Terminal puzzle
 */
class GeneratePuzzleTask(puzzleGeneration: PuzzleGeneration) : AsyncTask<Int, Void, Terminal>() {

    interface PuzzleGeneration {
        fun onPrePuzzleGeneration()
        fun onPostPuzzleGeneration(result: Terminal?)
    }

    private val mPuzzleGeneration: PuzzleGeneration = puzzleGeneration

    override fun onPreExecute() {
        mPuzzleGeneration.onPrePuzzleGeneration()
    }

    override fun doInBackground(vararg params: Int?): Terminal {
        val edge = params[0]!!
        val minSubGiven = params[1]!!
        val minTotalGiven = params[2]!!

        val s = Sudoku.genString(0, edge.toLong(), minSubGiven.toLong(), minTotalGiven.toLong())
        return Gson().fromJson(s, Terminal::class.java)
    }

    override fun onPostExecute(result: Terminal?) {
        mPuzzleGeneration.onPostPuzzleGeneration(result)
    }

    override fun onCancelled(result: Terminal?) {
        mPuzzleGeneration.onPostPuzzleGeneration(result)
    }

}