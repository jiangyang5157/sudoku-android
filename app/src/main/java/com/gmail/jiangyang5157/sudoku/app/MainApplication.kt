package com.gmail.jiangyang5157.sudoku.app

import android.app.Application
import android.content.res.Configuration
import com.gmail.jiangyang5157.sudoku.db.SqliteModule
import com.gmail.jiangyang5157.sudoku_presenter.SudokuModule

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
        AppInjector.create(
                AppModule(),
                SudokuModule(),
                SqliteModule())
    }

    /**
     * Called by the system when the device configuration changes while your component is running.
     */
    override fun onConfigurationChanged(newConfig: Configuration?) {
        super.onConfigurationChanged(newConfig)
    }

}
