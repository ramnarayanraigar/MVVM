package com.ramnarayan.data.request

import android.util.Log
import com.android.volley.ClientError
import com.android.volley.NoConnectionError
import com.android.volley.ServerError
import com.android.volley.VolleyError
import com.ramnarayan.data.request.CustomVolleyRequest
import com.ramnarayan.commons.Result
import com.ramnarayan.commons.UIConstants
import org.json.JSONObject
import kotlin.coroutines.Continuation
import kotlin.coroutines.resumeWithException


class CustomVolleyRequestWithParams(
    url:String,
    continuation: Continuation<Result<JSONObject>>,
    request: JSONObject
): CustomVolleyRequest(
    Method.GET, url,

    { response: JSONObject? ->
        Log.d("Response", response!!.toString())
        continuation.resumeWith(kotlin.Result.success(Result.Success(response) ))
    }, { error: VolleyError ->
        val response = error.networkResponse
        val errorMsg = if (error is ClientError) {
            UIConstants.clientError
        } else if (error is ServerError && response != null){
            UIConstants.serverError
        } else if (error is NoConnectionError) {
            UIConstants.noConnectionError
        } else {
            UIConstants.somethingWentWrong
        }
        continuation.resumeWithException(Exception(errorMsg))
    },
    null

    ) {

    override fun getHeaders(): MutableMap<String, String> {
        val headers: MutableMap<String, String> = HashMap()
        headers["Connection"] = "keep-alive"

        return headers
    }

    override fun parseNetworkError(volleyError: VolleyError): VolleyError {
        return volleyError.networkResponse?.let {
            Log.e("CUSTOM VOLLEY", volleyError.networkResponse.headers.toString())
            Log.e("CUSTOM VOLLEY", String(volleyError.networkResponse.data))
            VolleyError(String(volleyError.networkResponse.data))
        } ?: volleyError
    }
}