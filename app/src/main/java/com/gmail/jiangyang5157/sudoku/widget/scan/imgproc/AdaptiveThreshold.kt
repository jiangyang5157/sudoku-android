package com.gmail.jiangyang5157.sudoku.widget.scan.imgproc

import org.opencv.core.Mat
import org.opencv.imgproc.Imgproc

/**
 * Created by Yang Jiang on May 02, 2018
 */
data class AdaptiveThreshold(var maxValue: Double,
                             var adaptiveMethod: Int,
                             var thresholdType: Int,
                             var blockSize: Int,
                             var c: Double)
    : ImgConverter {

    override fun convert(src: Mat): Mat {
        val ret = Mat()
        Imgproc.adaptiveThreshold(src, ret, maxValue, adaptiveMethod, thresholdType, blockSize, c)
        return ret
    }

}