package com.gmail.jiangyang5157.sudoku.app

import com.google.inject.Guice
import com.google.inject.Injector
import com.google.inject.Module
import java.util.*

/**
 * Created by Yang Jiang on April 13, 2018
 */
object AppInjector {

    // todo hold feature injectors

    private var mInjector: Injector? = null

    fun getInjector() = mInjector

    fun create(vararg modules: Module) {
        mInjector = Guice.createInjector(Arrays.asList(*modules))
    }

    fun destroy() {
        mInjector = null
    }

}