package com.gmail.jiangyang5157.sudoku.app;

import android.app.Application;
import android.content.Context;
import android.content.res.Resources;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;

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

    @Provides
    Context contextProvider() {
        return mApplication.getApplicationContext();
    }

    @Provides
    Resources contextResources() {
        return mApplication.getResources();
    }

}