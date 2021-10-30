package com.ramnarayan.data.request

import com.android.volley.NetworkResponse
import com.android.volley.ParseError
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.HttpHeaderParser
import org.json.JSONException
import org.json.JSONObject
import java.io.UnsupportedEncodingException


open class CustomVolleyRequest internal constructor(
    requestMethod: Int, fileUrl: String?, listener: Response.Listener<JSONObject>,
    onErrorListener: Response.ErrorListener?, params: HashMap<String, String>?
) : Request<JSONObject?>(requestMethod, fileUrl, onErrorListener) {
    private var mParams: Map<String, String> = HashMap()
    private val mListener: Response.Listener<JSONObject>

    override fun getParams(): Map<String, String> {
        return mParams
    }

    override fun deliverResponse(response: JSONObject?) {
        mListener.onResponse(response)
    }

    init {
        setShouldCache(false)
        mListener = listener
        if (params != null) {
            mParams = params
        }
    }

    override fun parseNetworkResponse(response: NetworkResponse): Response<JSONObject?>? {
        return try {
            Response.success(JSONObject(String(response.data)), HttpHeaderParser.parseCacheHeaders(response))
        } catch (e: UnsupportedEncodingException) {
            Response.error(ParseError(e))
        } catch (je: JSONException) {
            Response.error(ParseError(je))
        }
    }

}