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

    override fun convert(src: Mat, dst: Mat) {
        Imgproc.adaptiveThreshold(src, dst, maxValue, adaptiveMethod, thresholdType, blockSize, c)
    }

}