package com.gmail.jiangyang5157.sudoku.widget.scan.imgproc

import android.graphics.Color
import org.opencv.core.*
import org.opencv.imgproc.Imgproc

/**
 * Created by Yang Jiang on May 02, 2018
 */
data class DrawContour(var scalar: Scalar, var thickness: Int) {

    companion object {
        fun buildScalar(color: Int) = Scalar(Color.red(color).toDouble(), Color.green(color).toDouble(), Color.blue(color).toDouble())
    }

    fun draw(src: Mat, contour: MatOfPoint) {
        Imgproc.drawContours(src, listOf(contour), 0, scalar, thickness)
    }

}