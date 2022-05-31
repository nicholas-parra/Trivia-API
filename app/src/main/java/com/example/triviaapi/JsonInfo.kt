package com.example.triviaapi

import com.google.gson.annotations.SerializedName

data class JsonInfo(
    @SerializedName("response_code")
    val responseCode : Int,
    val results : List<Question>)