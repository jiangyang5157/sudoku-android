package com.gmail.jiangyang5157.sudoku.testinjection;

import android.util.Log;

import com.google.inject.Inject;

/**
 * Created by Yang Jiang on April 15, 2018
 */
public class BImpl2 implements B {

    private int n = 0;
    private A a;

    @Inject
    public BImpl2(A a){
        this.a = a;

    }

    @Override
    public void sendMsg(String s) {
        n++;
        Log.d("####", s + " BImpl2.n=" + n);
    }
}
