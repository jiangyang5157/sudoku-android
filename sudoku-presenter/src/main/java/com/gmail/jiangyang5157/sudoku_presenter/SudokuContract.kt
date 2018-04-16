package com.gmail.jiangyang5157.sudoku_presenter

import com.gmail.jiangyang5157.sudoku_presenter.model.Terminal

/**
 * Created by Yang Jiang on April 14, 2018
 */
interface SudokuContract {

    interface PuzzleGeneration {

        fun onPrePuzzleGeneration()
        fun onPostPuzzleGeneration(result: Terminal?)
    }

    interface PuzzleResolution {

        fun onPrePuzzleResolution()
        fun onPostPuzzleResolution(result: Terminal?)
    }

    interface Presenter : PuzzleGeneration, PuzzleResolution {

        fun setView(view: View)

        fun generatePuzzle(edge: Int, minSubGiven: Int, minTotalGiven: Int)
        fun resolvePuzzle()

        fun getPuzzle() : Terminal?
        fun getProgress() : Terminal?
    }

    interface View : PuzzleGeneration, PuzzleResolution

}
