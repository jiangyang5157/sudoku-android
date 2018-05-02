package com.gmail.jiangyang5157.sudoku.widget.scan.imgproc

import org.opencv.core.Core
import org.opencv.core.Mat
import org.opencv.core.Size
import org.opencv.imgproc.Imgproc

/**
 * Created by Yang Jiang on May 02, 2018
 */
data class GaussianBlur(var kWidth: Double,
                        var kHeight: Double,
                        var sigmaX: Double,
                        var sigmaY: Double,
                        var boardType: Int = Core.BORDER_DEFAULT) : ImgConverter {

    override fun convert(src: Mat): Mat {
        val ret = Mat()
        Imgproc.GaussianBlur(src, ret, Size(kWidth, kHeight), sigmaX, sigmaY, boardType)
        return ret
    }

}