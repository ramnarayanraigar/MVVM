package com.ramnarayan.data.service

import android.content.Context
import com.ramnarayan.commons.Result
import com.ramnarayan.data.network.VolleyHelper
import com.ramnarayan.data.request.CustomVolleyRequestWithParams
import com.ramnarayan.data.room.DatabaseBuilder
import com.ramnarayan.data.room.entity.StackOverflowEntity
import kotlinx.coroutines.suspendCancellableCoroutine
import org.json.JSONObject

class StackOverflowQuestionService(private val context: Context) {

    suspend fun stackOverflowQuestionList(
        requestBody: JSONObject,
        url: String
    ): Result<JSONObject> {
        return suspendCancellableCoroutine { continuation ->
            run {
                val queue = VolleyHelper.getInstance(context).requestQueue
                val stringRequest = CustomVolleyRequestWithParams(url, continuation, requestBody)
                queue.add(stringRequest)
            }
        }
    }

    suspend fun insertIntoDB(data : StackOverflowEntity) {
        DatabaseBuilder.getInstance(context).stackOverflow().deleteAll()
        DatabaseBuilder.getInstance(context).stackOverflow().insertIntoStackOverflow(data)
    }

     suspend fun getAllData() : Result<List<StackOverflowEntity>> {
        val data = DatabaseBuilder.getInstance(context).stackOverflow().getAllStackOverflowEntity()
        return Result.Success(data)
    }
}