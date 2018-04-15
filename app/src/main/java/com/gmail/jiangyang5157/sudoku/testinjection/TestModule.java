package com.gmail.jiangyang5157.sudoku.testinjection;

import android.util.Log;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;

/**
 * Created by Yang Jiang on April 15, 2018
 */
/*
val modules: Array<Module> = arrayOf(
        SudokuModule(),
        TestModule())
AppInjector.create(*modules)

AppInjector.getInjector()?.getInstance(A::class.java)?.sendMsg("getInstance(A)")
AppInjector.getInjector()?.getInstance(B::class.java)?.sendMsg("getInstance(B)")
AppInjector.getInjector()?.getInstance(BImpl::class.java)?.sendMsg("getInstance(BImpl)")
AppInjector.getInjector()?.getInstance(BImpl2::class.java)?.sendMsg("getInstance(BImpl2)")

AppInjector.getInjector()?.getInstance(A::class.java)?.sendMsg("getInstance(A)*2")
AppInjector.getInjector()?.getInstance(B::class.java)?.sendMsg("getInstance(B)*2")
AppInjector.getInjector()?.getInstance(BImpl::class.java)?.sendMsg("getInstance(BImpl)*2")
AppInjector.getInjector()?.getInstance(BImpl2::class.java)?.sendMsg("getInstance(BImpl2)*2")

Log.d("####", AppInjector.getInjector()?.getInstance(SudokuPresenter::class.java).toString())

 */
public class TestModule extends AbstractModule {

    @Override
    protected void configure() {

        bind(A.class).to(AImpl.class);
        bind(B.class).to(BImpl2.class).in(Singleton.class);

    }

    @Provides
    AImpl AImpl() {
        Log.d("####", "Provides AImpl()");
        return new AImpl();
    }

    @Provides
    BImpl BImpl() {
        Log.d("####", "Provides BImpl()");
        return new BImpl(AImpl());
    }

    @Provides
    BImpl2 BImpl2() {
        Log.d("####", "Provides BImpl2()");
        return new BImpl2(AImpl());
    }
}
