package com.example.triviaapi

class Quiz(
    private val questionList : List<Question>,
    private var currentQuestionIndex : Int = 0,
    var score : Int = 0
) {
    fun getQuestionNumber() : Int {
        return questionList.size
    }

    fun questionsRemaining() : Boolean {
        return currentQuestionIndex < questionList.size
    }

    fun getCurrentQuestion() : Question {
        return questionList[currentQuestionIndex]
    }

    fun isCorrect(playerAnswer : String) : Boolean {
        var retval = true
        if (playerAnswer == getCurrentQuestion().correctAnswer) {
            score++
        } else {
            retval = false
        }
        return retval
    }

    fun advance() {
        currentQuestionIndex++
    }
}