package com.gmail.jiangyang5157.sudoku.ui

import android.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import com.gmail.jiangyang5157.sudoku.R
import com.gmail.jiangyang5157.sudoku.app.AppInjector
import com.gmail.jiangyang5157.sudoku_presenter.SudokuContract
import com.gmail.jiangyang5157.sudoku_presenter.model.Terminal

/**
 * Created by Yang Jiang on July 16, 2017
 */
class MainFragment : Fragment(), SudokuContract.View {

    private var tvNotification: TextView? = null

    private var mSudokuPresenter: SudokuContract.Presenter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mSudokuPresenter = AppInjector.getInjector()?.getInstance(SudokuContract.Presenter::class.java)
        mSudokuPresenter?.setView(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tvNotification = view?.findViewById(R.id.tv_notification) as TextView

        view.findViewById(R.id.btn_gen_puzzle)?.setOnClickListener {
            mSudokuPresenter?.generatePuzzle(
                    (view.findViewById(R.id.et_edge) as EditText).text.toString().toInt(),
                    (view.findViewById(R.id.et_min_sub_given) as EditText).text.toString().toInt(),
                    (view.findViewById(R.id.et_min_total_given) as EditText).text.toString().toInt()
            )
        }
        view.findViewById(R.id.btn_resolve_puzzle)?.setOnClickListener {
            mSudokuPresenter?.resolvePuzzle()
        }
    }

    override fun onPrePuzzleGeneration() {
        tvNotification?.text = "onPrePuzzleGeneration"
    }

    override fun onPostPuzzleGeneration(result: Terminal?) {
        tvNotification?.text = result?.toSquareString()
    }

    override fun onPrePuzzleResolution() {
        tvNotification?.text = "onPrePuzzleGeneration"
    }

    override fun onPostPuzzleResolution(result: Terminal?) {
        tvNotification?.text = result?.toSquareString()
    }

    companion object {
        const val TAG = "MainFragment"
    }

}