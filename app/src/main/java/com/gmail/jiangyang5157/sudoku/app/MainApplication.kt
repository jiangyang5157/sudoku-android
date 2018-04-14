package com.gmail.jiangyang5157.sudoku.app

import android.app.Application
import android.content.res.Configuration
import com.gmail.jiangyang5157.sudoku_presenter.SudokuPresenter
import com.google.inject.AbstractModule
import com.google.inject.Guice

/**
 * Created by Yang Jiang on April 14, 2018
 */
class MainApplication : Application() {

    /**
     * This is called when the overall system is running low on memory,
     * and would like actively running processes to tighten their belts.
     */
    override fun onLowMemory() {
        super.onLowMemory()
    }

    /**
     * Called when the application is starting,
     * before any other application objects have been created.
     */
    override fun onCreate() {
        super.onCreate()

        // todo
//        val modules: Array<Module> = arrayOf(SudokuModule())
//        AppInjector.create(*modules)
        // todo Remove
        var injector = Guice.createInjector(object : AbstractModule() {

            override fun configure() {
                bind(SudokuPresenter::class.java)
            }
        })
    }

    /**
     * Called by the system when the device configuration changes while your component is running.
     */
    override fun onConfigurationChanged(newConfig: Configuration?) {
        super.onConfigurationChanged(newConfig)
    }

}
