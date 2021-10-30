package com.ramnarayan.mvvm.di

import android.content.Context
import com.ramnarayan.domain.di.DomainDependencySetup
import com.ramnarayan.mvvm.ui.cocktail.CocktailListViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.core.context.loadKoinModules
import org.koin.core.context.startKoin
import org.koin.dsl.module
import javax.inject.Inject

object UIDependencySetup {

    private val uiModules = module {
        viewModel { CocktailListViewModel(get()) }
    }

    @JvmStatic
    @Inject
    fun inject(context: Context) {
        startKoin {
            androidContext(context)
        }

        loadKoinModules(
            uiModules
        )

        //setup dependency for Domain layer
        DomainDependencySetup.inject()
    }
}