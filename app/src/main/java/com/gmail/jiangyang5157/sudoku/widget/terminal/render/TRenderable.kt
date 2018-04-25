package com.gmail.jiangyang5157.sudoku.widget.terminal.render

import android.graphics.Canvas
import com.gmail.jiangyang5157.kotlin_kit.render.Renderable

/**
 * Created by Yang Jiang on April 25, 2018
 */
interface TRenderable : Renderable<Canvas> {
    val priority: Int
}
