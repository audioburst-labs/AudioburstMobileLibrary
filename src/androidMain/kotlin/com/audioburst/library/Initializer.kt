package com.audioburst.library

import android.content.Context
import androidx.startup.Initializer

internal lateinit var applicationContext: Context

internal class ContextInitializer : Initializer<Context> {

    override fun create(context: Context): Context {
        applicationContext = context.applicationContext
        return context
    }

    override fun dependencies(): List<Class<out Initializer<*>>> = emptyList()
}
