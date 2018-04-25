package com.gmail.jiangyang5157.sudoku.widget.terminal.render

import android.graphics.Canvas
import com.gmail.jiangyang5157.kotlin_kit.math.Vector2i
import com.gmail.jiangyang5157.kotlin_kit.render.scene.Particle

/**
 * Created by Yang Jiang on April 25, 2018
 */
open class TRect(override var position: Vector2i = Vector2i(),
                 override val priority: Int = 0)
    : Particle<Vector2i>, TRenderable {


    override fun onRender(t: Canvas) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}