package com.gmail.jiangyang5157.sudoku.widget.scan

import android.util.Size
import java.lang.Long.signum

/**
 * Created by Yang Jiang on April 30, 2018
 *
 * Compares two `Size`s based on their areas.
 */
internal class CompareSizesByArea : Comparator<Size> {

    // We cast here to ensure the multiplications won't overflow
    override fun compare(lhs: Size, rhs: Size) =
            signum(lhs.width.toLong() * lhs.height - rhs.width.toLong() * rhs.height)

}