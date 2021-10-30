package com.ramnarayan.data.network;

import android.content.Context;
import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;


public final class VolleyHelper {
    private final static int INITIALTIMEOUTMS = 60000;
    private final static int OTPONCALLTIMEOUTMS = 5000;
    private final static int WORLDTIMETIMEOUTMS = 0;
    private static VolleyHelper mInstance;
    private RequestQueue mRequestQueue;
    private static Context mCtx;
    private static final String TAG = "VolleyHelper";

    private VolleyHelper(Context context) {
        mCtx = context;
        mRequestQueue = getRequestQueue();
    }

    public static synchronized VolleyHelper getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new VolleyHelper(context);
            VolleyLog.DEBUG = false;
        }
        return mInstance;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            // getApplicationContext() is key, it keeps you from leaking the
            // Activity or BroadcastReceiver if someone passes one in.
            try {
                mRequestQueue = Volley.newRequestQueue(mCtx.getApplicationContext(), new HurlStack(null, new WSSSLSocketFactory()));
                mRequestQueue.getCache().clear();
            } catch (KeyManagementException | NoSuchAlgorithmException e) {
                Log.e(TAG, e.getMessage());
            }
        }
        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req) {
        getRequestQueue().add(req);
    }

    public void sendRequestToApi(final int method, final String url, final JSONObject body, final VolleyCallback callback) {
        StringRequest stringRequest = new StringRequest(method, url,
                response -> {
                    callback.onSuccessResponse(response, 200, false);
                    VolleyLog.e(TAG, response);
                }, error -> {
            NetworkResponse response = error.networkResponse;
            if (error instanceof ServerError && response != null) {
                try {
                    String res = new String(response.data,
                            HttpHeaderParser.parseCharset(response.headers, "utf-8"));
                    callback.onErrorResponse(res, response.statusCode);
                } catch (UnsupportedEncodingException e) {
                    VolleyLog.e(TAG, e.getMessage());
                    callback.onErrorResponse("error", -1);
                }
            } else if (error instanceof NoConnectionError) {
                callback.onErrorResponse("error", 408);
            } else if (response != null) {
                callback.onErrorResponse("error", response.statusCode);
            } else {
                callback.onErrorResponse("error", -1);
            }
        }) {

            @Override
            public String getBodyContentType() {
                return "application/json";
            }

            @Override
            public byte[] getBody() {

                return body != null ? body.toString().getBytes() : new JSONObject().toString().getBytes();

            }

            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Accept", "application/json");
                headers.put("Accept-Charset", "utf-8");
                headers.put("Content-Type", "application/json");
                return headers;
            }

        };

        //5 secs timeout is needed for otp api
        //for all other apis default timeout is 30 secs
        if (url.contains("creation/callOTP")) {
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(OTPONCALLTIMEOUTMS,
                    0,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            addToRequestQueue(stringRequest);
        } else if (url.contains("https://worldtimeapi.org")) {
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(WORLDTIMETIMEOUTMS,
                    0,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            addToRequestQueue(stringRequest);
        } else {
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(INITIALTIMEOUTMS,
                    0,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            addToRequestQueue(stringRequest);
        }
    }

    public void jsonFileRequest(final int method, final String url, final JSONObject body, final VolleyCallback callback) {

        Log.e(TAG, "File api process: jsonFileRequest CALL STARTED");
        try {

            CustomVolleyRequest request = new CustomVolleyRequest(method, url,
                    new Response.Listener<byte[]>() {
                        @Override
                        public void onResponse(byte[] response) {
                            Log.e(TAG, "File api process: volley got RESPONSE");
                            boolean isSuccess = true;
                            String filename = url.split("bookId=")[1] + ".zip";
                            StringBuilder text = new StringBuilder();
                            try {
                                if (response != null) {
                                    File zipFile = mCtx.getFileStreamPath(filename);

                                    FileOutputStream outputStream = mCtx.openFileOutput(filename, Context.MODE_PRIVATE);
                                    outputStream.write(response);
                                    outputStream.flush();
                                    outputStream.close();

                                    ZipFile fileZipped = new ZipFile(zipFile);
                                    InputStream inputStream = fileZipped.getInputStream(fileZipped.entries().nextElement());
                                    BufferedReader br = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
                                    String line;
                                    Log.e(TAG, "File api process: volley file WRITING STARTED");
                                    while ((line = br.readLine()) != null) {
                                        text.append(line);
                                        text.append("\n");
                                    }
                                    br.close();
                                    Log.e(TAG, "File api process: volley file WRITING ENDED");
                                }
                            } catch (ZipException e) {
                                callback.onErrorResponse("error ZipException", -1);
                                isSuccess = false;
                                Log.e(TAG, "jsonFileRequest: ", e);
                            } catch (UnsupportedEncodingException e) {
                                isSuccess = false;
                                callback.onErrorResponse(e.getMessage(), -1);
                                Log.e(TAG, "jsonFileRequest: ", e);
                            } catch (FileNotFoundException e) {
                                isSuccess = false;
                                callback.onErrorResponse(e.getMessage(), -1);
                                Log.e(TAG, "jsonFileRequest: ", e);
                            } catch (IOException e) {
                                isSuccess = false;
                                callback.onErrorResponse(e.getMessage(), -1);
                                Log.e(TAG, "jsonFileRequest: ", e);
                            } finally {
                                File zipFile = mCtx.getFileStreamPath(filename);
                                Log.e(TAG, "File api process: volley DELETE FILE");
                                if (zipFile.exists())
                                    zipFile.delete();
                                if (isSuccess) {
                                    String responseStr = text.toString();
                                    if (responseStr.isEmpty()) {
                                        callback.onErrorResponse(responseStr, -1);
                                    } else {
                                        callback.onSuccessResponse(text.toString(), 200, false);
                                        VolleyLog.e(TAG, text.toString());
                                    }
                                    Log.e(TAG, "File api process: volley SEND RESPONSE");

                                }
                            }
                        }
                    }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    NetworkResponse response = error.networkResponse;
                    if (error instanceof ServerError && response != null) {
                        try {
                            String res = new String(response.data,
                                    HttpHeaderParser.parseCharset(response.headers, "utf-8"));
                            callback.onErrorResponse(res, response.statusCode);
                        } catch (UnsupportedEncodingException e) {
                            VolleyLog.e(TAG, e.getMessage());
                            callback.onErrorResponse("error", -1);
                        }
                    } else if (error instanceof NoConnectionError) {
                        callback.onErrorResponse("error", 408);
                    } else if (response != null) {
                        callback.onErrorResponse("error", response.statusCode);
                    } else {
                        callback.onErrorResponse("error", -1);
                    }
                }


            }, null) {

                @Override
                public Map<String, String> getHeaders() {
                    Map<String, String> headers = new HashMap<>();

                    headers.put("Connection", "keep-alive");

                    Log.e(TAG, "File api process: volley network call STARTED");
                    return headers;
                }
            };
            request.setRetryPolicy(new DefaultRetryPolicy(INITIALTIMEOUTMS,
                    0,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            Log.e(TAG, "File api process: volley sending REQUEST");
            addToRequestQueue(request);
        } catch (Exception e) {
            Log.e(TAG, "jsonFileRequest:", e);
        }
    }

    private class CustomVolleyRequest extends Request<byte[]> {

        private Map<String, String> mParams;
        private final Response.Listener<byte[]> mListener;

        CustomVolleyRequest(int requestMethod, String fileUrl, Response.Listener<byte[]> listener,
                            Response.ErrorListener onErrorListener, HashMap<String, String> params) {

            super(requestMethod, fileUrl, onErrorListener);
            setShouldCache(false);
            mListener = listener;
            mParams = params;
        }


        @Override
        protected Response<byte[]> parseNetworkResponse(NetworkResponse response) {

            return Response.success(response.data, HttpHeaderParser.parseCacheHeaders(response));
        }

        @Override
        protected Map<String, String> getParams() {
            return mParams;
        }


        @Override
        protected void deliverResponse(byte[] response) {
            mListener.onResponse(response);
        }

    }


    public interface VolleyCallback {
        void onSuccessResponse(String result, int responseCode, boolean error);

        void onErrorResponse(String result, int responseCode);
    }
}
