package com.ramnarayan.mvvm

import android.app.Application
import com.ramnarayan.mvvm.di.UIDependencySetup

class Application : Application() {
    override fun onCreate() {
        super.onCreate()

        UIDependencySetup.inject(this)
    }
}