package com.annalisetarhan.kacaolur

import android.app.Application
import android.content.Context
import com.annalisetarhan.kacaolur.dagger.AppComponent
import com.annalisetarhan.kacaolur.dagger.DaggerAppComponent
import com.jakewharton.threetenabp.AndroidThreeTen

class Application: Application() {

    companion object {
        lateinit var appComponent: AppComponent
        lateinit var context: Context
    }

    override fun onCreate() {
        super.onCreate()

        context = applicationContext
        appComponent = DaggerAppComponent.factory().create(applicationContext)
        AndroidThreeTen.init(this)
    }
}
