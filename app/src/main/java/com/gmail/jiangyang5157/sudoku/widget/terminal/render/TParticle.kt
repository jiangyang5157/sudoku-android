package com.gmail.jiangyang5157.sudoku.widget.terminal.render

import com.gmail.jiangyang5157.kotlin_kit.math.Vector2i
import com.gmail.jiangyang5157.kotlin_kit.render.Particle

/**
 * Created by Yang Jiang on April 25, 2018
 */
interface TParticle : Particle<Vector2i>, TRenderable {
    var priority: Int
}