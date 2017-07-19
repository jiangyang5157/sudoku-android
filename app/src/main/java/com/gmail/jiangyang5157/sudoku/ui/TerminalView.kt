package com.gmail.jiangyang5157.sudoku.ui

import android.content.Context
import android.graphics.Canvas
import android.util.Log
import android.view.SurfaceHolder
import android.view.SurfaceView
import com.gmail.jiangyang5157.sudoku.render.RenderThread
import android.view.MotionEvent

/**
 * Created by Yang Jiang on July 18, 2017
 */
class TerminalView(ctx: Context) : SurfaceView(ctx), SurfaceHolder.Callback, RenderThread.OnRenderListener {

    private var renderThread: RenderThread? = null

    init {
        isClickable = true
        holder.addCallback(this)
    }

    override fun surfaceChanged(holder: SurfaceHolder?, format: Int, width: Int, height: Int) {
        Log.d(TAG, "surfaceChanged $width x $height")
        renderThread?.onRefresh()
    }

    override fun surfaceDestroyed(holder: SurfaceHolder?) {
        Log.d(TAG, "surfaceDestroyed")
        renderThread?.onStop()
    }

    override fun surfaceCreated(holder: SurfaceHolder?) {
        Log.d(TAG, "surfaceCreated")
        if (renderThread == null || renderThread!!.state === Thread.State.TERMINATED) {
            renderThread = RenderThread(60, holder!!, this)
        }
        renderThread!!.onStart()
        renderThread!!.onPause()
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> renderThread!!.onResume()
            MotionEvent.ACTION_UP -> {
                renderThread!!.onPause()
                renderThread!!.onRefresh()
            }
            MotionEvent.ACTION_MOVE -> {
            }
            MotionEvent.ACTION_CANCEL -> renderThread!!.onPause()
            else -> {
            }
        }

        return super.onTouchEvent(event)
    }

    override fun onRender(canvas: Canvas) {
        Log.d(TAG, "onRender")
    }

    companion object {
        val TAG = "TerminalView"
    }
}