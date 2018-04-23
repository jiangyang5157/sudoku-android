package com.gmail.jiangyang5157.sudoku_presenter

/**
 * Created by Yang Jiang on April 23, 2018
 */
class KeypadPresenter(override val mView: KeypadContract.View) : KeypadContract.Presenter {

    init {
        mView.setPresenter(this)
    }

    private var isPossibilityModeEnable = false

    override fun enterDigit(digit: Int) {
        if (digit < 0) {
            throw IllegalArgumentException()
        }

        if (isPossibilityModeEnable) {
            mView.mDelegate?.possibilityEnterd(digit)
        } else {
            mView.mDelegate?.digitEnterd(digit)
        }
    }

    override fun enterClear() {
        if (isPossibilityModeEnable) {
            mView.mDelegate?.possibilityCleard()
        } else {
            mView.mDelegate?.digitCleard()
        }
    }

    override fun switchPossibilityMode(enabled: Boolean) {
        isPossibilityModeEnable = enabled
        if (enabled) {
            mView.mDelegate?.possibilityModeEnabled()
        } else {
            mView.mDelegate?.possibilityModeDisabled()
        }
    }

}