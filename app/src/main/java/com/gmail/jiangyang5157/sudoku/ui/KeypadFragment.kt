package com.gmail.jiangyang5157.sudoku.ui

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Switch
import com.gmail.jiangyang5157.sudoku.R
import com.gmail.jiangyang5157.sudoku_presenter.KeypadContract
import com.gmail.jiangyang5157.sudoku_presenter.KeypadPresenter

/**
 * Created by Yang Jiang on April 23, 2018
 */
class KeypadFragment : Fragment(), KeypadContract.View {

    override var mDelegate: KeypadContract.View.Delegate? = null

    private var mKeypadPresenter: KeypadContract.Presenter = KeypadPresenter(this)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_keypad, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view?.findViewById(R.id.btn_keypad_1)?.setOnClickListener {
            mKeypadPresenter.enterDigit(1)
        }
        view?.findViewById(R.id.btn_keypad_2)?.setOnClickListener {
            mKeypadPresenter.enterDigit(2)
        }
        view?.findViewById(R.id.btn_keypad_3)?.setOnClickListener {
            mKeypadPresenter.enterDigit(3)
        }
        view?.findViewById(R.id.btn_keypad_4)?.setOnClickListener {
            mKeypadPresenter.enterDigit(4)
        }
        view?.findViewById(R.id.btn_keypad_5)?.setOnClickListener {
            mKeypadPresenter.enterDigit(5)
        }
        view?.findViewById(R.id.btn_keypad_6)?.setOnClickListener {
            mKeypadPresenter.enterDigit(6)
        }
        view?.findViewById(R.id.btn_keypad_7)?.setOnClickListener {
            mKeypadPresenter.enterDigit(7)
        }
        view?.findViewById(R.id.btn_keypad_8)?.setOnClickListener {
            mKeypadPresenter.enterDigit(8)
        }
        view?.findViewById(R.id.btn_keypad_9)?.setOnClickListener {
            mKeypadPresenter.enterDigit(9)
        }
        view?.findViewById(R.id.btn_keypad_clear)?.setOnClickListener {
            mKeypadPresenter.enterClear()
        }
        (view?.findViewById(R.id.switch_keypad_possibility) as Switch).setOnCheckedChangeListener { _, isChecked ->
            mKeypadPresenter.switchPossibilityMode(isChecked)
        }
    }

    override fun setPresenter(presenter: KeypadContract.Presenter) {
        mKeypadPresenter = presenter
    }

    fun setDelegate(delegate: KeypadContract.View.Delegate) {
        mDelegate = delegate
    }

}

