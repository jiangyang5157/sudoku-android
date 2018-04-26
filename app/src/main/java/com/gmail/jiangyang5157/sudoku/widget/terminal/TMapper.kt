package com.gmail.jiangyang5157.sudoku.widget.terminal

import com.gmail.jiangyang5157.kotlin_kit.math.Vector2i
import com.gmail.jiangyang5157.kotlin_kit.model.Mapper
import com.gmail.jiangyang5157.sudoku.widget.terminal.render.TTerminal
import com.gmail.jiangyang5157.sudoku.widget.terminal.render.spec.TCellSpec
import com.gmail.jiangyang5157.sudoku_presenter.model.Terminal

/**
 * Created by Yang Jiang on April 25, 2018
 */
data class TMapper(val width: Int, val height: Int) : Mapper<Terminal, TTerminal> {

    override fun map(from: Terminal): TTerminal {
        if (width != height) {
            throw IllegalStateException()
        }

        val tEdge = width
        val cEdge = tEdge / from.E
        val eSqrt = Math.sqrt(from.E.toDouble()).toInt()
        val pEdge = cEdge / eSqrt
        if (tEdge <= 0 || cEdge <= 0 || eSqrt <= 0 || pEdge <= 0) {
            throw IllegalStateException()
        }

        val ret = TTerminal(E = from.E)
        ret.edge = tEdge

        ret.C.forEachIndexed { itc, tc ->
            val tcCol = from.col(itc)
            val tcRow = from.row(itc)
            val tcX = tcCol * cEdge
            val tcY = tcRow * cEdge
            tc.position = Vector2i(tcX, tcY)
            tc.edge = cEdge
            from.C[itc]?.B?.apply {
                tc.B = this
            }
            from.C[itc]?.D?.apply {
                if (this != 0) {
                    tc.spec.flag.set(TCellSpec.FIXD)
                }
                tc.D = this
            }

            tc.P.forEachIndexed { itcp, tcp ->
                val tcpCol = itcp % eSqrt
                val tcpRow = itcp / eSqrt
                val tcpX = tcX + tcpCol * pEdge
                val tcpY = tcY + tcpRow * pEdge
                tcp.position = Vector2i(tcpX, tcpY)
                tcp.edge = pEdge
            }
        }

        return ret
    }

}