package com.gmail.jiangyang5157.sudoku_classifier

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.View
import com.gmail.jiangyang5157.kotlin_kit.render.Renderable
import java.util.*

class OverlayView : View {

    constructor(context: Context)
            : super(context)

    constructor(context: Context, attrs: AttributeSet)
            : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int)
            : super(context, attrs, defStyleAttr)

    private val renderables = LinkedList<Renderable<Canvas>>()

    fun addRenderable(callback: Renderable<Canvas>) {
        renderables.add(callback)
    }

    @Synchronized
    override fun draw(canvas: Canvas) {
        super.draw(canvas)
        for (renderable in renderables) {
            renderable.onRender(canvas)
        }
    }

}
