package com.ramnarayan.data.request

import android.util.Log
import com.android.volley.Request
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonObjectRequest
import org.json.JSONObject
import java.util.*
import kotlin.coroutines.Continuation
import kotlin.coroutines.resumeWithException
import com.ramnarayan.commons.Result

class JSONObjectRequestWithParameters(
    url:String,
    continuation: Continuation<Result<JSONObject>>,
    request:JSONObject):
        JsonObjectRequest(Request.Method.POST,url,request,

            { response: JSONObject? ->
                Log.e("Response", response!!.toString())
//                        val responseObject = GsonBuilder().create().fromJson(response, StatusResponse::class.java)
                continuation.resumeWith(kotlin.Result.success(Result.Success(response) ))
            }, { error: VolleyError ->
            Log.d("Response", error.toString())
                continuation.resumeWithException(Exception(error.message))
            }
            ){

            override fun getBodyContentType(): String {
                return "application/json"
            }

            override fun getHeaders(): MutableMap<String, String> {
                val headers: MutableMap<String, String> = HashMap()
                headers["Accept"] = "application/json"
                headers["Accept-Charset"] = "utf-8"
                headers["Content-Type"] = "application/json"

                return headers
            }

        }