package com.gmail.jiangyang5157.sudoku.ui

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.gmail.jiangyang5157.sudoku.R
import com.gmail.jiangyang5157.sudoku_presenter.SudokuContract
import com.gmail.jiangyang5157.sudoku_presenter.model.Terminal

/**
 * Created by Yang Jiang on April 21, 2018
 */
class SudokuFragment : Fragment(), SudokuContract.View {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_sudoku, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // TODO
    }

    override fun puzzleGenerated(t: Terminal?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun terminalReveald(t: Terminal?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun progressUpdated(index: Int, d: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun progressResolved(t: Terminal?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun possibilityUpdated(index: Int, possibility: IntArray) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun cellSelected(index: Int, relevant: IntArray) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun possibilityEnterEnabled() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun possibilityEnterDisabled() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun digitCleard() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun possibilityCleard() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun digitEnterd(digit: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun possibilityEnterd(digit: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun setPresenter(presenter: SudokuContract.Presenter) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}