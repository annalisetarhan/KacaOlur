package com.annalisetarhan.kacaolur

import android.app.Application
import android.content.res.Resources
import com.jakewharton.threetenabp.AndroidThreeTen

class ApplicationClass: Application() {

    override fun onCreate() {
        super.onCreate()

        AndroidThreeTen.init(this)
    }
}