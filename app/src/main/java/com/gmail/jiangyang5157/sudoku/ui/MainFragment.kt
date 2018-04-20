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
 */
class MainFragment : Fragment(), SudokuContract.View {

    private var mSudokuPresenter: SudokuContract.Presenter

    private var tvNotification: TextView? = null

    private var testSelectCell = 0

    init {
        mSudokuPresenter = SudokuPresenter(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tvNotification = view?.findViewById(R.id.tv_notification) as TextView

        mSudokuPresenter.start()
        view.findViewById(R.id.btn_get_puzzle)?.setOnClickListener {
            mSudokuPresenter.getPuzzle(
                    (view.findViewById(R.id.et_edge) as EditText).text.toString().toInt(),
                    (view.findViewById(R.id.et_min_sub_given) as EditText).text.toString().toInt(),
                    (view.findViewById(R.id.et_min_total_given) as EditText).text.toString().toInt()
            )
        }
        view.findViewById(R.id.btn_get_terminal)?.setOnClickListener {
            mSudokuPresenter.getTerminal()
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
        view.findViewById(R.id.btn_select_cell)?.setOnClickListener {
            mSudokuPresenter.selectCell(testSelectCell++)
        }
    }

    override fun showPuzzle(t: Terminal) {
        tvNotification?.text = t.toSquareString() + "\n^----------------^ showPuzzle\n"
    }

    override fun showTerminal(t: Terminal) {
        tvNotification?.text = t.toSquareString() + "\n^----------------^ showTerminal\n"
    }

    override fun showUpdatedProgress(index: Int, d: Int) {
        tvNotification?.text = "index=" + index + "\nd=" + d + "\n^----------------^ showUpdatedProgress\n"
    }

    override fun showResolvedProgress(t: Terminal) {
        tvNotification?.text = t.toSquareString() + "\n^----------------^ showResolvedProgress\n"
    }

    override fun showUpdatedPossibility(index: Int, possibility: IntArray) {
        tvNotification?.text = "index=" + index + "\npossibility=" + Arrays.toString(possibility) + "\n^----------------^ showUpdatedPossibility\n"
    }

    override fun showSelectedCell(index: Int, relevant: IntArray) {
        tvNotification?.text = "index=" + index + "\nrelevant=" + Arrays.toString(relevant) + "\n^----------------^ showSelectedCell\n"
    }

    override fun setPresenter(presenter: SudokuContract.Presenter) {
        mSudokuPresenter = presenter
    }

}