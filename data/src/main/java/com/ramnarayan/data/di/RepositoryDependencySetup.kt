package com.ramnarayan.data.di

import com.ramnarayan.data.repository.StackOverflowQuestionRepository
import com.ramnarayan.data.service.StackOverflowQuestionService
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.loadKoinModules
import org.koin.dsl.module

object RepositoryDependencySetup {

    private val repoModules = module {
        single { StackOverflowQuestionRepository(get()) }
        single { StackOverflowQuestionService(androidContext()) }
    }

    fun inject() {
        loadKoinModules(
            listOf(repoModules)
        )
    }
}