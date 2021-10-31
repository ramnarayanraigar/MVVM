package com.ramnarayan.data.repository

import com.ramnarayan.commons.Result
import com.ramnarayan.data.api.API
import com.ramnarayan.data.room.entity.StackOverflowEntity
import com.ramnarayan.data.service.StackOverflowQuestionService
import org.json.JSONObject

class StackOverflowQuestionRepository(private val stackOverflowQuestionService: StackOverflowQuestionService) {

    suspend fun stackOverflowQuestion(): Result<JSONObject> {
        val url: String = API.stackOverflowQuestion
        val list = stackOverflowQuestionService.stackOverflowQuestionList(JSONObject(), url)

        when (list) {
            is Result.Success -> {
                insertIntoDB(list.data)
            }

            is Result.Failure -> {

            }
        }


        return list
    }

    private suspend fun insertIntoDB(jsonObject: JSONObject) {
        stackOverflowQuestionService.insertIntoDB(StackOverflowEntity(0, jsonObject.toString()))
    }

    suspend fun getAllData(): Result<List<StackOverflowEntity>> {
        return stackOverflowQuestionService.getAllData()
    }
}