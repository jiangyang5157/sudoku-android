package com.gmail.jiangyang5157.sudoku_presenter

/**
 * Created by Yang Jiang on April 13, 2018
 */
class SudokuPresenter : SudokuContract.Presenter {



    override fun start() {
        // TODO
    }

    override fun generateSudoku(edge: Int, minSubGiven: Int, minTotalGiven: Int) {
        // TODO
    }

//    private lateinit var mView: SudokuContract.View
//
//    private var mPuzzle: Terminal? = null
//    private var mProgress: Terminal? = null
//
//    private var mGeneratePuzzleTask: GeneratePuzzleTask? = null
//    private var mResolvePuzzleTask: ResolvePuzzleTask? = null
//
//
//    override fun setView(view: SudokuContract.View) {
//        mView = view
//    }
//
//    override fun generatePuzzle(edge: Int, minSubGiven: Int, minTotalGiven: Int) {
//        if (mGeneratePuzzleTask?.status != AsyncTask.Status.FINISHED) {
//            mGeneratePuzzleTask?.cancel(true)
//        }
//        if (edge <= 0 || minSubGiven < 0 || minTotalGiven < 0) {
//            return
//        }
//        mGeneratePuzzleTask = GeneratePuzzleTask(this)
//        mGeneratePuzzleTask?.execute(edge, minSubGiven, minTotalGiven)
//    }
//
//    override fun resolvePuzzle() {
//        if (mResolvePuzzleTask?.status != AsyncTask.Status.FINISHED) {
//            mResolvePuzzleTask?.cancel(true)
//        }
//        if (mPuzzle == null) {
//            return
//        }
//        mResolvePuzzleTask = ResolvePuzzleTask(this)
//        mResolvePuzzleTask?.execute(mPuzzle)
//    }
//
//    override fun onPrePuzzleGeneration() {
//        mView.onPrePuzzleGeneration()
//    }
//
//    override fun onPostPuzzleGeneration(result: Terminal?) {
//        mPuzzle = result
//        mProgress = mPuzzle?.copy()
//        mView.onPostPuzzleGeneration(mPuzzle)
//    }
//
//    override fun onPrePuzzleResolution() {
//        mView.onPrePuzzleResolution()
//    }
//
//    override fun onPostPuzzleResolution(result: Terminal?) {
//        mProgress = result
//        mView.onPostPuzzleResolution(result)
//    }
//
//    override fun getPuzzle(): Terminal? {
//        return mPuzzle
//    }
//
//    override fun getProgress(): Terminal? {
//        return mProgress
//    }

}
