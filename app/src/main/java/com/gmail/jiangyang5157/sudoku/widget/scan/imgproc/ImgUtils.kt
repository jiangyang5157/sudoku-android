package com.gmail.jiangyang5157.sudoku.widget.scan.imgproc

import org.opencv.core.Mat
import org.opencv.imgproc.Imgproc

object ImgUtils {

    fun gray2rgb(src: Mat, dst: Mat) {
        Imgproc.cvtColor(src, dst, Imgproc.COLOR_GRAY2RGB)
    }

}