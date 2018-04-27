package com.gmail.jiangyang5157.sudoku.widget

import android.content.Context
import android.util.AttributeSet
import android.widget.Button
import android.widget.FrameLayout
import android.widget.RelativeLayout
import android.widget.Switch
import com.gmail.jiangyang5157.sudoku.R

/**
 * Created by Yang Jiang on April 23, 2018
 */
class KeypadView : FrameLayout {

    interface Callback {

        fun digitEnterd(digit: Int)

        fun digitCleard()

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
            findViewById(R.id.btn_keypad_clear)?.setOnClickListener {
                enterClear()
            }
            (findViewById(R.id.switch_keypad_possibility) as Switch).setOnCheckedChangeListener { _, isChecked ->
                isPossibilityModeEnable = isChecked
            }
        }
    }

    fun setSize(size: Int) {
        if (size <= 1) {
            throw IllegalArgumentException()
        }
        val sqrtS = Math.sqrt(size.toDouble()).toInt()

        findViewById(R.id.keypadview_digit_container)?.apply {
            val relativeLayout = this as RelativeLayout
            relativeLayout.removeAllViews()

            val keypadBtnNormal = resources.getDimension(R.dimen.keypad_btn_normal).toInt()
            val btns = Array(size) { Button(context) }
            btns.forEachIndexed { i, btn ->
                relativeLayout.addView(btn.apply {
                    id = i + 1
                    text = (i + 1).toString()
                    setOnClickListener { enterDigit(i + 1) }
                    when {
                        i == 0 -> {
                            layoutParams = RelativeLayout.LayoutParams(keypadBtnNormal, keypadBtnNormal)
                        }
                        i < sqrtS -> {
                            layoutParams = RelativeLayout.LayoutParams(keypadBtnNormal, keypadBtnNormal).apply {
                                addRule(RelativeLayout.RIGHT_OF, btns[i - 1].id)
                            }
                        }
                        i % sqrtS == 0 ->
                            layoutParams = RelativeLayout.LayoutParams(keypadBtnNormal, keypadBtnNormal).apply {
                                addRule(RelativeLayout.BELOW, btns[i - sqrtS].id)
                            }
                        else -> {
                            layoutParams = RelativeLayout.LayoutParams(keypadBtnNormal, keypadBtnNormal).apply {
                                addRule(RelativeLayout.RIGHT_OF, btns[i - 1].id)
                                addRule(RelativeLayout.BELOW, btns[i - sqrtS].id)
                            }
                        }
                    }
                })
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
}

