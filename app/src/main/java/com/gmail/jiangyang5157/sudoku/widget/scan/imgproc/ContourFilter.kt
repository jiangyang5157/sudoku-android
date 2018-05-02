package com.gmail.jiangyang5157.sudoku.widget.scan.imgproc

import org.opencv.core.MatOfPoint

/**
 * Created by Yang Jiang on May 02, 2018
 */
interface ContourFilter {

    fun filter(contours: List<MatOfPoint>): MatOfPoint?

}