package com.wafflestudio.snuboard.utils

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.ResponseBody

fun parseErrorResponse(responseBody: ResponseBody): ErrorResponse {
    val gson = Gson()
    val type = object : TypeToken<ErrorResponse>() {}.type
    return gson.fromJson(responseBody.charStream(), type)
}

data class ErrorResponse(
        val statusCode: Int,
        val message: String
)
