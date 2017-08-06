package com.gmail.jiangyang5157.sudoku.ui

import android.content.Context
import android.util.Log
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.view.MotionEvent
import com.gmail.jiangyang5157.kotlin_core.render.RenderThread

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
            renderThread = RenderThread(60, this)
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

    override fun onRender() {
        Log.d(TAG, "onRender")
        if (holder.surface.isValid) {
            val canvas = holder.lockCanvas(null)
            // TODO
            holder.unlockCanvasAndPost(canvas)
        }
    }

    companion object {
        val TAG = "TerminalView"
    }
}