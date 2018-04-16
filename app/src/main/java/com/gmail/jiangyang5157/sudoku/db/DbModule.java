package com.gmail.jiangyang5157.sudoku.db;

import android.content.Context;

import com.google.inject.AbstractModule;

/**
 * Created by Yang Jiang on April 17, 2018
 */
public class DbModule extends AbstractModule {

    private Context mContext;

    public DbModule(Context ctx) {
        mContext = ctx;
    }

    @Override
    protected void configure() {

//        bind(PuzzleDbApi.class);//.in(Singleton.class);

    }

//    @Provides
//    PuzzleDbApi contextPuzzleDbApi() {
//        PuzzleDbHelper puzzleDbHelper = new PuzzleDbHelper(mContext);
//        return new PuzzleDbApi(puzzleDbHelper);
//    }
//
//    @Provides
//    Context contextProvider() {
//        return mContext;
//    }

}
