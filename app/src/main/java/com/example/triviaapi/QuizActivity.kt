package com.example.triviaapi

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.triviaapi.databinding.ActivityQuizBinding

class QuizActivity : AppCompatActivity() {
    companion object {
        val EXTRA_QUESTIONS = "questions"
        val TAG = "QuizActivity"
    }
    lateinit var binding : ActivityQuizBinding
    lateinit var game : Quiz

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityQuizBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.groupQuizUi.visibility = View.VISIBLE
        binding.textViewQuizFinalScore.visibility = View.GONE

        val questions = intent.getParcelableArrayListExtra<Question>(EXTRA_QUESTIONS) as List<Question>
        Log.d(TAG, "onCreate: $questions")

        game = Quiz(questions)
        updateText(game.getCurrentQuestion())

        binding.buttonQuizTrue.setOnClickListener {
            buttonFunctionality("True")
        }

        binding.buttonQuizFalse.setOnClickListener {
            buttonFunctionality("False")
        }
    }

    private fun buttonFunctionality(answer : String) {
        val response = getResponse(answer)
        Toast.makeText(this, response, Toast.LENGTH_SHORT).show()
        game.advance()
        if (game.questionsRemaining()) {
            updateText(game.getCurrentQuestion())
        } else {
            reportScore()
        }
    }

    fun updateText(currentQuestion : Question) {
        binding.textViewQuizCurrentQuestion.text = currentQuestion.question
        binding.textViewQuizCurrentScore.text = "Current score: ${game.score} / ${game.getQuestionNumber()}"
    }

    fun getResponse(answer : String) : String {
        return if (game.isCorrect(answer)) {
            "Correct!"
        } else {
            "False!"
        }
    }

    fun reportScore() {
        Log.d(TAG, "reportScore: :D")
        binding.groupQuizUi.visibility = View.GONE
        binding.textViewQuizFinalScore.text = "Final score:\n${game.score} / ${game.getQuestionNumber()}"
        binding.textViewQuizFinalScore.visibility = View.VISIBLE
    }
}