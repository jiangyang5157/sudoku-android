package com.gmail.jiangyang5157.sudoku.widget.scan.imgproc

import org.opencv.core.Mat
import org.opencv.imgproc.Imgproc

/**
 * Created by Yang Jiang on May 02, 2018
 */
data class Canny(var threshold1: Double,
                 var threshold2: Double,
                 var apertureSize: Int,
                 var l2gradient: Boolean = false) : ImgConverter {

    override fun convert(src: Mat): Mat {
        val ret = Mat()
        Imgproc.Canny(src, ret, threshold1, threshold2, apertureSize, l2gradient)
        return ret
    }

}