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
    }

    private var mSudokuPresenter: SudokuContract.Presenter = SudokuPresenter(this)
    private var mTerminalView: TerminalView? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_sudoku, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view?.apply {
            (findViewById(R.id.view_keypad) as KeypadView).setCallback(keypadViewCallback)
            mTerminalView = findViewById(R.id.view_terminal) as TerminalView?
            mTerminalView?.isClickable = true
        }

        // TODO Implement start logic
        mSudokuPresenter.generatePuzzle(9, 4, 55)
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
        mTerminalView?.setTerminal(terminal)
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