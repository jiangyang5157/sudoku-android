package com.gmail.jiangyang5157.sudoku.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout

/**
 * Created by Yang Jiang on April 21, 2018
 */
class SquareFrameLayout : FrameLayout {

    constructor(context: Context?)
            : super(context)

    constructor(context: Context?, attrs: AttributeSet?)
            : super(context, attrs)

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int)
            : super(context, attrs, defStyleAttr)

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int)
            : super(context, attrs, defStyleAttr, defStyleRes)

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val width = View.MeasureSpec.getSize(widthMeasureSpec)
        val height = View.MeasureSpec.getSize(heightMeasureSpec)
        if (height > width) {
            super.onMeasure(widthMeasureSpec, widthMeasureSpec)
            setMeasuredDimension(width, width)
        } else {
            super.onMeasure(heightMeasureSpec, heightMeasureSpec)
            setMeasuredDimension(height, height)
        }
    }

}