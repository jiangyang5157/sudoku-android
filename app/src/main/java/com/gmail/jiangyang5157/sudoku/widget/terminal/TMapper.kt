package com.gmail.jiangyang5157.sudoku.widget.terminal

import com.gmail.jiangyang5157.kotlin_kit.math.Vector2i
import com.gmail.jiangyang5157.kotlin_kit.model.Mapper
import com.gmail.jiangyang5157.sudoku.widget.terminal.render.TTerminal
import com.gmail.jiangyang5157.sudoku_presenter.model.Terminal

/**
 * Created by Yang Jiang on April 25, 2018
 */
data class TMapper(val width: Int, val height: Int) : Mapper<Terminal, TTerminal> {

    override fun map(from: Terminal): TTerminal {
        if (width != height) {
            throw IllegalStateException()
        }
        val tLen = width
        val cLen = tLen / from.E
        val sqrtE = Math.sqrt(from.E.toDouble()).toInt()
        val pLen = cLen / sqrtE
        if (tLen <= 0 || cLen <= 0 || sqrtE <= 0 || pLen <= 0) {
            throw IllegalStateException()
        }

        val ret = TTerminal(from.E, w = tLen, h = tLen)

        ret.C.forEachIndexed { itc, tc ->

            val tcCol = from.col(itc)
            val tcRow = from.row(itc)
            val tcX = tcCol * cLen
            val tcY = tcRow * cLen
            tc.position = Vector2i(tcX, tcY)
            tc.w = cLen
            tc.h = cLen
            from.C[itc]?.D?.apply {
                tc.spec.digit = this
            }

            tc.P.forEachIndexed { itcp, tcp ->

                val tcpCol = itcp % sqrtE
                val tcpRow = itcp / sqrtE
                val tcpX = tcX + tcpCol * pLen
                val tcpY = tcY + tcpRow * pLen
                tcp.position = Vector2i(tcpX, tcpY)
                tcp.w = pLen
                tcp.h = pLen
            }
        }

        return ret
    }

}