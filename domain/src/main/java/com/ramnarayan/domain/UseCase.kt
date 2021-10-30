package com.ramnarayan.domain

import com.ramnarayan.commons.Result

interface UseCase<I,O : Any> {
    suspend fun execute(input: I): Result<O>
}
