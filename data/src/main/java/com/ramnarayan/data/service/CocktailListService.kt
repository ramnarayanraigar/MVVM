package com.ramnarayan.data.service

import android.content.Context
import com.ramnarayan.data.network.VolleyHelper
import kotlinx.coroutines.suspendCancellableCoroutine
import org.json.JSONObject
import com.ramnarayan.commons.Result
import com.ramnarayan.data.request.CustomVolleyRequestWithParams


class CocktailListService(private val context: Context) {

    suspend fun cocktailList(requestBody: JSONObject, url: String): Result<JSONObject> {

        return suspendCancellableCoroutine { continuation ->
            run {
                val queue = VolleyHelper.getInstance(context).requestQueue
                val stringRequest = CustomVolleyRequestWithParams(url, continuation, requestBody)
                queue.add(stringRequest)
            }
        }
    }

}