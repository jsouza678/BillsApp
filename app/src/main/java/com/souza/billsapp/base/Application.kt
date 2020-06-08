package com.souza.billsapp.base

import android.app.Application
import com.souza.billsapp.expensecatalog.di.expenseCatalogModule
import com.souza.billsapp.incomecatalog.di.incomeCatalogModule
import org.koin.android.BuildConfig
import org.koin.android.ext.koin.androidContext
import org.koin.android.logger.AndroidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.EmptyLogger
import org.koin.core.logger.Logger

class Application : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@Application)
            logger(koinLogger())
            modules(
                modules = listOf(
                    expenseCatalogModule,
                    incomeCatalogModule
                )
            )
        }
    }

    private fun koinLogger(): Logger = if (BuildConfig.DEBUG) AndroidLogger() else EmptyLogger()
}
