package com.gmail.jiangyang5157.sudoku.widget.terminal

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.PixelFormat
import android.graphics.PorterDuff
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import com.gmail.jiangyang5157.kotlin_android_kit.widget.RenderView
import com.gmail.jiangyang5157.kotlin_kit.render.Renderable

/**
 * Created by Yang Jiang on July 18, 2017
 */
class TerminalView : RenderView, Renderable<Canvas> {

    companion object {
        const val TAG = "TerminalView"
    }

//    private val drawableTerminal: MutableList<TParticle> = mutableListOf()

    init {
        setZOrderOnTop(true)
        holder.setFormat(PixelFormat.TRANSPARENT)
        setRenderable(this)

//        drawableTerminal.add(TCell(spec = TCellNormal(1),position= Vector2i(50,140), w=50, h=50, priority = 1))
//        drawableTerminal.add(TCell(spec = TCellRelevant(1),position= Vector2i(150,170), w=50, h=50, priority = 0))
//        drawableTerminal.add(TCell(spec = TCellRelevant(1),position= Vector2i(250,160), w=50, h=50, priority = 2))
//        drawableTerminal.add(TCell(spec = TCellNormal(1),position= Vector2i(350,100), w=50, h=50, priority = 5))
//        drawableTerminal.add(TCell(spec = TCellNormal(1),position= Vector2i(450,110), w=50, h=50, priority = 4))
//        drawableTerminal.add(TCell(spec = TCellNormal(1),position= Vector2i(550,120), w=50, h=50, priority = 7))
//        drawableTerminal.add(TCell(spec = TCellNormal(1),position= Vector2i(650,130), w=50, h=50, priority = 2))
//        drawableTerminal.sortBy { it.priority }
    }

    constructor(context: Context)
            : super(context)

    constructor(context: Context, attrs: AttributeSet)
            : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int)
            : super(context, attrs, defStyleAttr)

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                Log.d(TAG, "MotionEvent.ACTION_DOWN")
                refreshRender()
            }
            MotionEvent.ACTION_UP -> {
                Log.d(TAG, "MotionEvent.ACTION_UP")
                refreshRender()
            }
            MotionEvent.ACTION_MOVE -> {
                refreshRender() // TODO conditional refresh
            }
        }
        return super.onTouchEvent(event)
    }

    override fun onRender(t: Canvas) {
        Log.d(TAG, "onRender")
        t.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR)

//        drawableTerminal.forEach {
//            Log.d(TAG, it.priority.toString())
//            it.onRender(t)
//        }
    }

}