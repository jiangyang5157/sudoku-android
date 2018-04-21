package com.gmail.jiangyang5157.sudoku.ui

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import com.gmail.jiangyang5157.sudoku.R
import com.gmail.jiangyang5157.sudoku_presenter.SudokuContract
import com.gmail.jiangyang5157.sudoku_presenter.SudokuPresenter
import com.gmail.jiangyang5157.sudoku_presenter.model.Terminal
import java.util.*

/**s
 * Created by Yang Jiang on July 16, 2017
 *
 * TODO Delete
 */
class DebugSudokuView : Fragment(), SudokuContract.View {

    private var mSudokuPresenter: SudokuContract.Presenter = SudokuPresenter(this)

    private var tvNotification: TextView? = null

    private var testSelectCell = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_debug_sudoku_view, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tvNotification = view?.findViewById(R.id.tv_notification) as TextView

        view.findViewById(R.id.btn_get_puzzle)?.setOnClickListener {
            mSudokuPresenter.generatePuzzle(
                    (view.findViewById(R.id.et_edge) as EditText).text.toString().toInt(),
                    (view.findViewById(R.id.et_min_sub_given) as EditText).text.toString().toInt(),
                    (view.findViewById(R.id.et_min_total_given) as EditText).text.toString().toInt()
            )
        }
        view.findViewById(R.id.btn_get_terminal)?.setOnClickListener {
            mSudokuPresenter.revealTerminal()
        }
        view.findViewById(R.id.btn_update_progress)?.setOnClickListener {
            mSudokuPresenter.updateProgress(0, 1)
        }
        view.findViewById(R.id.btn_resolve_progress)?.setOnClickListener {
            mSudokuPresenter.resolveProgress()
        }
        view.findViewById(R.id.btn_update_possibility)?.setOnClickListener {
            mSudokuPresenter.updatePossibility(0, (Math.random() * (view.findViewById(R.id.et_edge) as EditText).text.toString().toInt()).toInt())
        }
        view.findViewById(R.id.btn_clear_possibility)?.setOnClickListener {
            mSudokuPresenter.clearPossibility(0)
        }
        view.findViewById(R.id.btn_select_cell)?.setOnClickListener {
            mSudokuPresenter.selectCell(testSelectCell++)
        }
        view.findViewById(R.id.btn_enter_digit)?.setOnClickListener {
            mSudokuPresenter.enterDigit((Math.random() * (view.findViewById(R.id.et_edge) as EditText).text.toString().toInt()).toInt())
        }
        view.findViewById(R.id.btn_enter_clear)?.setOnClickListener {
            mSudokuPresenter.enterClear()
        }
        view.findViewById(R.id.btn_invert_possibility_enter_status)?.setOnClickListener {
            mSudokuPresenter.invertPossibilityEnterStatus()
        }
    }

    override fun puzzleGenerated(t: Terminal?) {
        tvNotification?.text = t?.toSquareString() + "\n^----------------^ puzzleGenerated\n"
    }

    override fun terminalReveald(t: Terminal?) {
        tvNotification?.text = t?.toSquareString() + "\n^----------------^ terminalReveald\n"
    }

    override fun progressUpdated(index: Int, d: Int) {
        tvNotification?.text = "index=" + index + "\nd=" + d + "\n^----------------^ progressUpdated\n"
    }

    override fun progressResolved(t: Terminal?) {
        tvNotification?.text = t?.toSquareString() + "\n^----------------^ progressResolved\n"
    }

    override fun possibilityUpdated(index: Int, possibility: IntArray) {
        tvNotification?.text = "index=" + index + "\npossibility=" + Arrays.toString(possibility) + "\n^----------------^ possibilityUpdated\n"
    }

    override fun cellSelected(index: Int, relevant: IntArray) {
        tvNotification?.text = "index=" + index + "\nrelevant=" + Arrays.toString(relevant) + "\n^----------------^ cellSelected\n"
    }

    override fun possibilityEnterEnabled() {
        tvNotification?.text = "\n^----------------^ possibilityEnterEnabled\n"
    }

    override fun possibilityEnterDisabled() {
        tvNotification?.text = "\n^----------------^ possibilityEnterDisabled\n"
    }

    override fun digitCleard() {
        tvNotification?.text = "\n^----------------^ digitCleard\n"
    }

    override fun possibilityCleard() {
        tvNotification?.text = "\n^----------------^ possibilityCleard\n"
    }

    override fun digitEnterd(digit: Int) {
        tvNotification?.text = "digit+=" + digit + "\n^----------------^ digitEnterd\n"
    }

    override fun possibilityEnterd(digit: Int) {
        tvNotification?.text = "possibility+=" + digit + "\n^----------------^ possibilityEnterd\n"
    }

    override fun setPresenter(presenter: SudokuContract.Presenter) {
        mSudokuPresenter = presenter
    }

}