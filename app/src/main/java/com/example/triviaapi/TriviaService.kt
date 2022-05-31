package com.example.triviaapi

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface TriviaService {
    @GET("api.php")
    fun getData(@Query("amount") amount : Int,
                @Query("category") category : Int,
                @Query("difficulty") difficulty : String,
                @Query("type") type: String = "boolean") : Call<JsonInfo>
}
