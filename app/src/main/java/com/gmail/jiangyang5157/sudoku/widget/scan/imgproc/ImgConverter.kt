package com.gmail.jiangyang5157.sudoku.widget.scan.imgproc

import org.opencv.core.Mat

/**
 * Created by Yang Jiang on May 02, 2018
 */
interface ImgConverter {

    fun convert(src: Mat, dst: Mat)

}