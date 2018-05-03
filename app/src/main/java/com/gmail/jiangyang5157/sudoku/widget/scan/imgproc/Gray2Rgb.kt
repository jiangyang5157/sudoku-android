package com.gmail.jiangyang5157.sudoku.widget.scan.imgproc

import org.opencv.core.Mat
import org.opencv.imgproc.Imgproc

/**
 * Created by Yang Jiang on May 02, 2018
 */
object Gray2Rgb : ImgConverter {

    override fun convert(src: Mat, dst: Mat) {
        Imgproc.cvtColor(src, dst, Imgproc.COLOR_GRAY2RGB)
    }

}