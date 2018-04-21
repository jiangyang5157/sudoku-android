package com.gmail.jiangyang5157.sudoku_presenter

/**
 * Created by Yang Jiang on April 21, 2018
 */
class EditorPresenter(view: SudokuContract.EditorView) : SudokuContract.EditorPresenter {

    private val mView = view

    init {
        mView.setPresenter(this)
    }

    private var isPossibilityEnabled = false

    override fun enablePossibility() {
        isPossibilityEnabled = true
        mView.possibilityEnabled()
    }

    override fun disablePossibility() {
        isPossibilityEnabled = false
        mView.possibilityDisabled()
    }

    override fun triggerPossibilityEnablement() {
        if (isPossibilityEnabled) {
            enablePossibility()
        } else {
            disablePossibility()
        }
    }

    override fun clear() {
        if (isPossibilityEnabled) {
            mView.possibilityCleard()
        } else {
            mView.digitCleard()
        }
    }

    override fun input(digit: Int) {
        if (isPossibilityEnabled) {
            mView.possibilityInputd(digit)
        } else {
            mView.digitInputd(digit)
        }
    }

}