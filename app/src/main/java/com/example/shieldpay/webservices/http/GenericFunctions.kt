package com.example.shieldpay.webservices.http

import android.util.Log
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL

private const val BASE_URL = "https://reqres.in/api/"
const val SUCCESS_RESPONSE_CODE = 200
const val FAILURE_RESPONSE_CODE = 400

enum class APIs(
    val url: String,
    val responseCode: Int,
    val requestMethod: String
) {
    RegisterAPI("${BASE_URL}register", SUCCESS_RESPONSE_CODE, "POST"),
    LoginAPI("${BASE_URL}login", SUCCESS_RESPONSE_CODE, "POST")
}

enum class APISelectionType(var apiType: String) {
    SelectAPI("Retrofit")
}

interface APIResult<T, Error> {
    fun onSuccess(success: T)
    fun onFailure(failure: Error)
}

interface Callbacks<T, Error> {
    fun onSuccess(success: T)
    fun onFailure(failure: Error)
    fun onFailure(failure: String)
}

class GenericFunctions {

    companion object {

        private fun <T> requestWithHttp(
            api: APIs,
            parameters: JSONObject,
            res: APIResult<T, Error>,
            modelClass: Class<T>
        ) {
            CoroutineScope(Dispatchers.IO).launch {
                val url = URL(api.url)
                Log.d("url", url.toString())
                (url.openConnection() as HttpURLConnection).apply {
                    requestMethod = api.requestMethod
                    setRequestProperty("Content-Type", "application/json")
                    val outputStream = OutputStreamWriter(outputStream)
                    outputStream.write(parameters.toString())
                    outputStream.flush()
                    when (responseCode) {
                        api.responseCode -> {
                            BufferedReader(InputStreamReader(inputStream)).use {
                                val result = Gson().fromJson(it.readText(), modelClass)
                                CoroutineScope(Dispatchers.Main).launch {
                                    if (result != null)
                                        res.onSuccess(result)
                                }
                            }
                        }
                        FAILURE_RESPONSE_CODE -> {
                            BufferedReader(InputStreamReader(errorStream)).use {
                                val result = Gson().fromJson(it.readText(), Error::class.java)
                                CoroutineScope(Dispatchers.Main).launch {
                                    if (result != null)
                                        res.onFailure(result)
                                }
                            }
                        }
                    }
                }
            }
        }

        fun httpRegister(
            data: JSONObject,
            apiResult: APIResult<RegisterUserResponse, Error>,
        ) {
            requestWithHttp(
                APIs.RegisterAPI,
                data,
                apiResult,
                RegisterUserResponse::class.java
            )
        }

        fun httpLogin(data: JSONObject, apiResult: APIResult<LoginUserResponse, Error>) {
            requestWithHttp(
                APIs.LoginAPI,
                data,
                apiResult,
                LoginUserResponse::class.java
            )
        }
    }

}