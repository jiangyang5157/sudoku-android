package com.gmail.jiangyang5157.sudoku.testinjection;

import android.util.Log;

/**
 * Created by Yang Jiang on April 15, 2018
 */
public class AImpl implements A {

    private int n = 0;

    @Override
    public void sendMsg(String s) {
        n++;
        Log.d("####", s + " AImpl.n=" + n);
    }
}
