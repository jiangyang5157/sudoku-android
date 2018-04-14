package com.gmail.jiangyang5157.sudoku_presenter.module;

import com.gmail.jiangyang5157.sudoku_presenter.SudokuContract;
import com.gmail.jiangyang5157.sudoku_presenter.SudokuPresenter;
import com.google.inject.AbstractModule;
import com.google.inject.Singleton;

/**
 * Created by Yang Jiang on April 14, 2018
 */
public class SudokuModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(SudokuContract.Presenter.class)
                .to(SudokuPresenter.class)
                .in(Singleton.class);
    }

}