package com.gmail.jiangyang5157.sudoku.widget.scan.imgproc

import org.opencv.core.Core
import org.opencv.core.Mat
import org.opencv.core.Size
import org.opencv.imgproc.Imgproc

/**
 * Created by Yang Jiang on May 02, 2018
 */
data class GaussianBlur(var kSize: Size,
                        var sigmaX: Double,
                        var sigmaY: Double,
                        var boardType: Int = Core.BORDER_DEFAULT)
    : ImgConverter {

    companion object {
        fun buildSize(k: Double) = Size(k, k)
    }

    override fun convert(src: Mat, dst: Mat) {
        Imgproc.GaussianBlur(src, dst, kSize, sigmaX, sigmaY, boardType)
    }

}