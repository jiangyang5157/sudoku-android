package com.gmail.jiangyang5157.sudoku.app;

import android.app.Application;

import com.google.inject.AbstractModule;

/**
 * Created by Yang Jiang on April 17, 2018
 */
public class AppModule extends AbstractModule {

    private Application mApplication;

    public AppModule(Application application) {
        mApplication = application;
    }

    @Override
    protected void configure() {
        for (Class<?> c = mApplication.getClass();
             c != null && Application.class.isAssignableFrom(c);
             c = c.getSuperclass()) {
            bind((Class<Object>) c).toInstance(mApplication);
        }


    }

}