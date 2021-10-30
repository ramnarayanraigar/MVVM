package com.ramnarayan.data.di

import com.ramnarayan.data.repository.CocktailListRepository
import com.ramnarayan.data.service.CocktailListService
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.loadKoinModules
import org.koin.dsl.module

object RepositoryDependencySetup {

    private val repoModules = module {
        single { CocktailListRepository(get()) }
        single { CocktailListService(androidContext()) }
    }

    fun inject() {
        loadKoinModules(
            listOf(repoModules)
        )
    }
}