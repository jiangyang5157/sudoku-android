package com.gmail.jiangyang5157.sudoku.ui

import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.gmail.jiangyang5157.sudoku.R
import com.gmail.jiangyang5157.sudoku.widget.KeypadView
import com.gmail.jiangyang5157.sudoku.widget.terminal.TerminalView
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

        const val KEY_EDGE = "KEY_BUNDLE_EDGE"
        const val KEY_MIN_SUB_GIVEN = "KEY_BUNDLE_MIN_SUB_GIVEN"
        const val KEY_MIN_TOTAL_GIVEN = "KEY_BUNDLE_MIN_TOTAL_GIVEN"
    }

    private var mSudokuPresenter: SudokuContract.Presenter = SudokuPresenter(this)
    private var mKeypadView: KeypadView? = null
    private var mTerminalView: TerminalView? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_sudoku, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.apply {
            try {
                val edge = arguments.getString(KEY_EDGE).toInt()
                val msg = arguments.getString(KEY_MIN_SUB_GIVEN).toInt()
                val mtg = arguments.getString(KEY_MIN_TOTAL_GIVEN).toInt()
                val sqrtE = Math.sqrt(edge.toDouble()).toInt()
                if (edge > 1 && edge == sqrtE * sqrtE && msg >= 0 && mtg >= 0) {
                    mSudokuPresenter.generatePuzzle(edge, msg, mtg)
                } else {
                    activity.finish()
                }

                view?.apply {
                    mKeypadView = findViewById(R.id.view_keypad) as KeypadView
                    mKeypadView?.setSize(edge)
                    mKeypadView?.setCallback(keypadViewCallback)
                    mTerminalView = findViewById(R.id.view_terminal) as TerminalView?
                    mTerminalView?.isClickable = true
                }
            } catch (e: NumberFormatException) {
                activity.finish()
            }
        }
    }

    private val keypadViewCallback = object : KeypadView.Callback {

        override fun digitEnterd(digit: Int) {
            Log.d(TAG, "digitEnterd: $digit")
            mTerminalView?.getSelectedCell()?.apply {
                mSudokuPresenter.updateProgress(this, digit)
            }
        }

        override fun digitCleard() {
            Log.d(TAG, "digitCleard")
            mTerminalView?.getSelectedCell()?.apply {
                mSudokuPresenter.updateProgress(this, 0)
            }
        }

        override fun possibilityEnterd(digit: Int) {
            Log.d(TAG, "possibilityEnterd: $digit")
            mTerminalView?.getSelectedCell()?.apply {
                mSudokuPresenter.updatePossibility(this, digit)
            }
        }

        override fun possibilityCleard() {
            Log.d(TAG, "possibilityCleard")
            mTerminalView?.getSelectedCell()?.apply {
                mSudokuPresenter.clearPossibility(this)
            }
        }
    }

    override fun puzzleGenerated(puzzle: Terminal?) {
        Log.d(TAG, "puzzleGenerated: $puzzle")
        mTerminalView?.setTerminal(puzzle)
    }

    override fun terminalReveald(terminal: Terminal?) {
        Log.d(TAG, "terminalReveald: $terminal")
    }

    override fun progressUpdated(index: Int, digit: Int) {
        Log.d(TAG, "progressUpdated: $index, $digit")
        mTerminalView?.setCell(index, digit)
    }

    override fun progressCleard(progress: Terminal) {
        Log.d(TAG, "progressCleard: $progress")
        mTerminalView?.setTerminal(progress)
    }

    override fun possibilityUpdated(index: Int, possibility: Array<Int?>) {
        Log.d(TAG, "possibilityUpdated: $index, " + Arrays.toString(possibility))
        mTerminalView?.setPossibility(index, possibility)
    }

    override fun setPresenter(presenter: SudokuContract.Presenter) {
        mSudokuPresenter = presenter
    }

}