package com.gmail.jiangyang5157.sudoku_presenter;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;

/**
 * Created by Yang Jiang on April 14, 2018
 */
public class SudokuModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(SudokuContract.Presenter.class).to(SudokuPresenter.class);
    }

    @Provides
    SudokuPresenter SudokuPresenter() {
        return new SudokuPresenter();
    }

}