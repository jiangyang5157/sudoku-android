package com.gmail.jiangyang5157.sudoku.widget.scan.imgproc

import org.opencv.core.MatOfPoint
import org.opencv.imgproc.Imgproc

/**
 * Created by Yang Jiang on May 02, 2018
 */
object MaxAreaContourFilter : ContourFilter {

    override fun filter(contours: List<MatOfPoint>): MatOfPoint? {
        if (contours.isEmpty()) {
            return null
        }

        var maxArea = 0.0
        var maxAreaIndex = -1
        contours.forEachIndexed { i, contour ->
            Imgproc.contourArea(contour).apply {
                if (this > maxAreaIndex) {
                    maxArea = this
                    maxAreaIndex = i
                }
            }
        }

        return contours[maxAreaIndex]
    }

}