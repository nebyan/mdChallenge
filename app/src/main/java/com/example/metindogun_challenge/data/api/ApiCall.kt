package com.example.metindogun_challenge.data.api

import retrofit2.Response
import java.lang.Exception

suspend fun<T: Any> apiCall(call: suspend() -> Response<T>): Result<T>{

    val response: Response<T>
    try {
        response = call.invoke()
    } catch (e: Exception){
        return Result.Error("Network Error")
    }

    return if (response.isSuccessful && response.body()!= null){
        Result.Success(data = response.body()!!)
    } else {
        Result.Error("Something went wrong!")
    }

}

sealed class Result<out T: Any>{
    data class Success<out T: Any>(val data: T) : Result<T>()
    data class Error(val errorMsg: String): Result<Nothing>()
}