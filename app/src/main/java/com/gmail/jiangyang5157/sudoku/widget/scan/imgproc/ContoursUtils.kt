package com.gmail.jiangyang5157.sudoku.widget.scan.imgproc

import android.graphics.Color
import org.opencv.core.Mat
import org.opencv.core.MatOfPoint
import org.opencv.core.Scalar
import org.opencv.imgproc.Imgproc

/**
 * Created by Yang Jiang on May 02, 2018
 */
object ContoursUtils {

    fun buildScalar(color: Int) = Scalar(Color.red(color).toDouble(), Color.green(color).toDouble(), Color.blue(color).toDouble())

    fun findExternals(src: Mat, dst: ArrayList<MatOfPoint>, hierarchy: Mat) {
        Imgproc.findContours(src, dst, hierarchy, Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE)
    }

    fun sortedByDescendingArea(contours: List<MatOfPoint>) {
        contours.sortedByDescending {
            Imgproc.contourArea(it)
        }
    }

}