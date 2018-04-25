package com.gmail.jiangyang5157.sudoku.widget.terminal.render.rect

import com.gmail.jiangyang5157.kotlin_kit.render.scene.Projection
import com.gmail.jiangyang5157.sudoku.widget.terminal.render.config.TParticle

/**
 * Created by Yang Jiang on April 25, 2018
 */
interface TRect<in Spec : TRectSpec, in Texture>
    : TParticle, Projection<TRectShape, Spec, Texture>
