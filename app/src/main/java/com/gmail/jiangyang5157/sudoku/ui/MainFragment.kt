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
    }

    override fun puzzleGenerated(t: Terminal) {
        tvNotification?.text = t.toSquareString() + "\n^----------------^ puzzleGenerated\n"
    }

    override fun terminalRevealed(t: Terminal) {
        tvNotification?.text = t.toSquareString() + "\n^----------------^ terminalRevealed\n"
    }

    override fun progressUpdated(index: Int, d: Int) {
        tvNotification?.text = "index=" + index + "\nd=" + d + "\n^----------------^ progressUpdated\n"
    }

    override fun progressResolved(t: Terminal) {
        tvNotification?.text = t.toSquareString() + "\n^----------------^ progressResolved\n"
    }

    override fun possibilityUpdated(index: Int, possibility: IntArray) {
        tvNotification?.text = "index=" + index + "\npossibility=" + Arrays.toString(possibility) + "\n^----------------^ possibilityUpdated\n"
    }

    override fun cellSelected(index: Int, relevant: IntArray) {
        tvNotification?.text = "index=" + index + "\nrelevant=" + Arrays.toString(relevant) + "\n^----------------^ cellSelected\n"
    }

    override fun setPresenter(presenter: SudokuContract.Presenter) {
        mSudokuPresenter = presenter
    }

}