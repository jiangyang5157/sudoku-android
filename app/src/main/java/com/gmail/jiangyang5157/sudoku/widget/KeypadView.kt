package com.gmail.jiangyang5157.sudoku.widget

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import android.widget.Switch
import com.gmail.jiangyang5157.sudoku.R

/**
 * Created by Yang Jiang on April 23, 2018
 */
class KeypadView : FrameLayout {

    interface Callback {

        fun digitEnterd(digit: Int)

        fun digitCleard()

        fun possibilityModeEnabled()

        fun possibilityModeDisabled()

        fun possibilityEnterd(digit: Int)

        fun possibilityCleard()
    }

    private var mCallback: Callback? = null
    fun getCallback() = mCallback
    fun setCallback(callback: Callback) {
        mCallback = callback
    }

    private var isPossibilityModeEnable = false

    constructor(context: Context?)
            : super(context) {
        init()
    }

    constructor(context: Context?, attrs: AttributeSet?)
            : super(context, attrs) {
        init()
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int)
            : super(context, attrs, defStyleAttr) {
        init()
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int)
            : super(context, attrs, defStyleAttr, defStyleRes) {
        init()
    }

    private fun init() {
        val view = inflate(context, R.layout.view_keypad, this)
        view?.apply {
            findViewById(R.id.btn_keypad_1)?.setOnClickListener {
                enterDigit(1)
            }
            findViewById(R.id.btn_keypad_2)?.setOnClickListener {
                enterDigit(2)
            }
            findViewById(R.id.btn_keypad_3)?.setOnClickListener {
                enterDigit(3)
            }
            findViewById(R.id.btn_keypad_4)?.setOnClickListener {
                enterDigit(4)
            }
            findViewById(R.id.btn_keypad_5)?.setOnClickListener {
                enterDigit(5)
            }
            findViewById(R.id.btn_keypad_6)?.setOnClickListener {
                enterDigit(6)
            }
            findViewById(R.id.btn_keypad_7)?.setOnClickListener {
                enterDigit(7)
            }
            findViewById(R.id.btn_keypad_8)?.setOnClickListener {
                enterDigit(8)
            }
            findViewById(R.id.btn_keypad_9)?.setOnClickListener {
                enterDigit(9)
            }
            findViewById(R.id.btn_keypad_clear)?.setOnClickListener {
                enterClear()
            }
            (findViewById(R.id.switch_keypad_possibility) as Switch).setOnCheckedChangeListener { _, isChecked ->
                switchPossibilityMode(isChecked)
            }
        }
    }

    private fun enterDigit(digit: Int) {
        if (digit < 0) {
            throw IllegalArgumentException()
        }

        if (isPossibilityModeEnable) {
            mCallback?.possibilityEnterd(digit)
        } else {
            mCallback?.digitEnterd(digit)
        }
    }

    private fun enterClear() {
        if (isPossibilityModeEnable) {
            mCallback?.possibilityCleard()
        } else {
            mCallback?.digitCleard()
        }
    }

    private fun switchPossibilityMode(enabled: Boolean) {
        isPossibilityModeEnable = enabled
        if (enabled) {
            mCallback?.possibilityModeEnabled()
        } else {
            mCallback?.possibilityModeDisabled()
        }
    }

}

