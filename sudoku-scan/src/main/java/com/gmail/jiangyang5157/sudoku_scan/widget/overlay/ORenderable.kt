package com.gmail.jiangyang5157.sudoku_scan.widget.overlay

import android.graphics.Canvas
import android.graphics.Paint
import com.gmail.jiangyang5157.kotlin_kit.render.Renderable

/**
 * Created by Yang Jiang on May 07, 2018
 */
interface ORenderable : Renderable<Canvas> {
    var paint: Paint
}
