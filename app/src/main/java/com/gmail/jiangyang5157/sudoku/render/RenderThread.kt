package com.gmail.jiangyang5157.sudoku.render

import android.graphics.Canvas
import android.util.Log
import android.view.SurfaceHolder
import com.gmail.jiangyang5157.kotlin_core.render.FrameRate

/**
 * Created by Yang Jiang on July 16, 2017
 */
class RenderThread(fps: Int, holder: SurfaceHolder, listener: RenderingListener) : Thread() {

    interface RenderingListener {
        fun onRender(canvas: Canvas)
    }

    private val frameRate = FrameRate(fps)
    private val surfaceHolder = holder
    private val renderingListener = listener
    private val lock = java.lang.Object()

    private var status = 0
    val ON_RUNNING = 1 shl 0
    val ON_PAUSED = 1 shl 1
    val ON_FOCUSED = 1 shl 2
    val ON_REFRESH = 1 shl 3

    private fun on(status: Int) {
        this.status = this.status or status
    }

    private fun off(status: Int) {
        this.status = this.status and status.inv()
    }

    fun check(status: Int): Boolean {
        return this.status and status == status
    }

    override fun run() {
        while (true) {
            while (check(ON_RUNNING) && (check(ON_PAUSED) || !check(ON_FOCUSED))) {
                if (check(ON_REFRESH)) {
                    off(ON_REFRESH)
                    break
                }
                synchronized(lock) {
                    lock.wait()
                }
            }

            if (!check(ON_RUNNING)) {
                break
            }

            if (!frameRate.newFrame()) {
                continue
            }

            if (surfaceHolder.surface.isValid) {
                val canvas = surfaceHolder.lockCanvas(null)
                paint(canvas)
                surfaceHolder.unlockCanvasAndPost(canvas)
            }
        }
    }

    fun paint(canvas: Canvas) {
        synchronized(lock) {
            renderingListener.onRender(canvas)
        }
    }

    fun onStart() {
        synchronized(lock) {
            Log.d(TAG, "onStart")
            on(ON_RUNNING)
            start()
        }
    }

    fun onStop() {
        synchronized(lock) {
            Log.d(TAG, "onStop")
            off(ON_RUNNING)
            lock.notifyAll()
        }

        var retry = true
        while (retry) {
            Log.d(TAG, "onStop - join")
            join()
            retry = false
        }
    }

    fun onPause() {
        synchronized(lock) {
            Log.d(TAG, "onPause")
            on(ON_PAUSED)
        }
    }

    fun onResume() {
        synchronized(lock) {
            Log.d(TAG, "onResume")
            off(ON_PAUSED)
            lock.notifyAll()
        }
    }

    fun onRefresh() {
        synchronized(lock) {
            Log.d(TAG, "onRefresh")
            on(ON_REFRESH)
            lock.notifyAll()
        }
    }

    fun onFocused() {
        synchronized(lock) {
            Log.d(TAG, "onFocused")
            on(ON_FOCUSED)
            lock.notifyAll()
        }
    }

    fun onUnfocused() {
        synchronized(lock) {
            Log.d(TAG, "onUnfocused")
            off(ON_FOCUSED)
        }
    }

    companion object {
        val TAG = "RenderThread"
    }
}