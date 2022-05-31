package com.example.triviaapi

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Question(var question : String, @SerializedName("correct_answer") val correctAnswer : String) : Parcelable
