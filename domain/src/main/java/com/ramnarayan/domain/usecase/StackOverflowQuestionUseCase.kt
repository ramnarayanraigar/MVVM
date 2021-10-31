package com.ramnarayan.domain.usecase

import com.google.gson.Gson
import com.ramnarayan.commons.Result
import com.ramnarayan.data.repository.StackOverflowQuestionRepository
import com.ramnarayan.domain.UseCase
import com.ramnarayan.domain.entities.StackOverflowQuestionListData
import kotlin.reflect.javaType
import kotlin.reflect.typeOf

class StackOverflowQuestionUseCase(private val stackOverflowQuestionRepository: StackOverflowQuestionRepository) :
    UseCase<String, StackOverflowQuestionListData> {
    @ExperimentalStdlibApi
    override suspend fun execute(input: String): Result<StackOverflowQuestionListData> {
        return when (val result =
            stackOverflowQuestionRepository.stackOverflowQuestion()) {

            is Result.Success -> {
                val chapterListType = typeOf<StackOverflowQuestionListData>().javaType
                val chapterList = Gson().fromJson(result.data.toString(), chapterListType) as StackOverflowQuestionListData
                Result.Success(chapterList)
            }

            is Result.Failure -> {
                Result.Failure(result.exception)
            }
        }
    }
}