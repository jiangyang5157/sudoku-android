package com.gmail.jiangyang5157.sudoku.widget.scan.imgproc

import org.opencv.core.Mat
import org.opencv.core.MatOfPoint
import org.opencv.imgproc.Imgproc

/**
 * Created by Yang Jiang on May 02, 2018
 */
object ContoursUtils {

    fun findExternals(src: Mat): List<MatOfPoint> {
        val ret = arrayListOf<MatOfPoint>()
        val hierarchy = Mat()
        Imgproc.findContours(src, ret, hierarchy, Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE)
        hierarchy.release()
        return ret
    }

    fun findIndexOfMaxArea(contours: List<MatOfPoint>): Int {
        var maxArea = 0.0
        var indexOfMaxArea = -1
        contours.forEachIndexed { i, contour ->
            Imgproc.contourArea(contour).apply {
                if (this > maxArea) {
                    maxArea = this
                    indexOfMaxArea = i
                }
            }
        }
        return indexOfMaxArea
    }

}