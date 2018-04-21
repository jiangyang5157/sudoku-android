package com.gmail.jiangyang5157.sudoku.ui

import android.graphics.*
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Switch
import com.gmail.jiangyang5157.kotlin_android_kit.widget.RenderView
import com.gmail.jiangyang5157.sudoku.R
import com.gmail.jiangyang5157.sudoku.widget.TerminalView
import com.gmail.jiangyang5157.sudoku_presenter.SudokuContract
import com.gmail.jiangyang5157.sudoku_presenter.SudokuPresenter
import com.gmail.jiangyang5157.sudoku_presenter.model.Terminal
import java.util.*

/**
 * Created by Yang Jiang on April 21, 2018
 */
class SudokuFragment : Fragment(), SudokuContract.View {

    companion object {

        const val TAG = "SudokuFragment"
    }

    private var mSudokuPresenter: SudokuContract.Presenter = SudokuPresenter(this)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_sudoku, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onTerminalViewCreated(view)
        onKeypadCreated(view)
    }

    private fun onTerminalViewCreated(view: View?) {
        val terminalView = (view?.findViewById(R.id.terminalview) as TerminalView)
        terminalView.setZOrderOnTop(true)
        terminalView.holder.setFormat(PixelFormat.TRANSPARENT)
        terminalView.setOnRenderCallback(onRenderCallback)
    }

    private fun onKeypadCreated(view: View?) {
        view?.findViewById(R.id.btn_keypad_1)?.setOnClickListener {
            mSudokuPresenter.enterDigit(1)
        }
        view?.findViewById(R.id.btn_keypad_2)?.setOnClickListener {
            mSudokuPresenter.enterDigit(2)
        }
        view?.findViewById(R.id.btn_keypad_3)?.setOnClickListener {
            mSudokuPresenter.enterDigit(3)
        }
        view?.findViewById(R.id.btn_keypad_4)?.setOnClickListener {
            mSudokuPresenter.enterDigit(4)
        }
        view?.findViewById(R.id.btn_keypad_5)?.setOnClickListener {
            mSudokuPresenter.enterDigit(5)
        }
        view?.findViewById(R.id.btn_keypad_6)?.setOnClickListener {
            mSudokuPresenter.enterDigit(6)
        }
        view?.findViewById(R.id.btn_keypad_7)?.setOnClickListener {
            mSudokuPresenter.enterDigit(7)
        }
        view?.findViewById(R.id.btn_keypad_8)?.setOnClickListener {
            mSudokuPresenter.enterDigit(8)
        }
        view?.findViewById(R.id.btn_keypad_9)?.setOnClickListener {
            mSudokuPresenter.enterDigit(9)
        }
        view?.findViewById(R.id.btn_keypad_clear)?.setOnClickListener {
            mSudokuPresenter.enterClear()
        }
        (view?.findViewById(R.id.switch_keypad_possibility) as Switch).setOnCheckedChangeListener { _, _ ->
            mSudokuPresenter.invertPossibilityEnterStatus()
        }
    }

    private val onRenderCallback = object : RenderView.OnRenderListener {

        override fun onRender(canvas: Canvas) {
            canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR)

            // TODO
            val paint = Paint()
            paint.color = Color.BLUE
            paint.textSize = 100F
            canvas.drawText("Hello world.", 200F, 200F, paint)
        }
    }

    override fun puzzleGenerated(t: Terminal?) {
        Log.d(TAG, "puzzleGenerated: $t")
    }

    override fun terminalReveald(t: Terminal?) {
        Log.d(TAG, "terminalReveald: $t")
    }

    override fun progressUpdated(index: Int, d: Int) {
        Log.d(TAG, "progressUpdated: $index, $d")
    }

    override fun progressResolved(t: Terminal?) {
        Log.d(TAG, "progressResolved: $t")
    }

    override fun possibilityUpdated(index: Int, possibility: IntArray) {
        Log.d(TAG, "possibilityUpdated: $index, " + Arrays.toString(possibility))
    }

    override fun cellSelected(index: Int, relevant: IntArray) {
        Log.d(TAG, "cellSelected: $index, " + Arrays.toString(relevant))
    }

    override fun possibilityEnterEnabled() {
        Log.d(TAG, "possibilityEnterEnabled")
    }

    override fun possibilityEnterDisabled() {
        Log.d(TAG, "possibilityEnterDisabled")
    }

    override fun digitCleard() {
        Log.d(TAG, "digitCleard")
    }

    override fun possibilityCleard() {
        Log.d(TAG, "possibilityCleard")
    }

    override fun digitEnterd(digit: Int) {
        Log.d(TAG, "digitEnterd: $digit")
    }

    override fun possibilityEnterd(digit: Int) {
        Log.d(TAG, "possibilityEnterd: $digit")
    }

    override fun setPresenter(presenter: SudokuContract.Presenter) {
        Log.d(TAG, "setPresenter: " + presenter.javaClass.simpleName)
    }

}