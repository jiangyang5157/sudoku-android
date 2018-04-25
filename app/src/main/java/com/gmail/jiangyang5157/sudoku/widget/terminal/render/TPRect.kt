package com.gmail.jiangyang5157.sudoku.widget.terminal.render

import com.gmail.jiangyang5157.kotlin_kit.math.Vector2i

/**
 * Created by Yang Jiang on April 25, 2018
 */
interface TPRect : TParticle {
    var w: Int
    var h: Int

    fun scale(size: Int) {
        position -= Vector2i(size, size)
        w += size * 2
        h += size * 2
    }

}
