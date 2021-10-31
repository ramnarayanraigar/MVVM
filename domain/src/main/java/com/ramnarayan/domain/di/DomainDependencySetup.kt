package com.ramnarayan.domain.di

import com.ramnarayan.data.di.RepositoryDependencySetup
import com.ramnarayan.domain.usecase.GetStackOverflowDataFromDbUseCase
import com.ramnarayan.domain.usecase.StackOverflowQuestionUseCase
import org.koin.core.context.loadKoinModules
import org.koin.dsl.module

object DomainDependencySetup {

    private val useCaseModules = module {
        factory { StackOverflowQuestionUseCase(get()) }
        factory { GetStackOverflowDataFromDbUseCase(get()) }
    }

    fun inject() {
        loadKoinModules(
            useCaseModules
        )

        //inject dependency for data layer
        RepositoryDependencySetup.inject()
    }
}