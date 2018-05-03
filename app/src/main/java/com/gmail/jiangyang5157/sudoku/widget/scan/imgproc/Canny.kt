package com.gmail.jiangyang5157.sudoku.widget.scan.imgproc

import org.opencv.core.Mat
import org.opencv.imgproc.Imgproc

/**
 * Created by Yang Jiang on May 02, 2018
 */
data class Canny(var threshold1: Double,
                 var threshold2: Double,
                 var apertureSize: Int,
                 var l2gradient: Boolean)
    : ImgConverter {

    override fun convert(src: Mat, dst: Mat) {
        Imgproc.Canny(src, dst, threshold1, threshold2, apertureSize, l2gradient)
    }

}