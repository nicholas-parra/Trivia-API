package com.example.triviaapi

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.triviaapi.databinding.ActivityMainBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {
    lateinit var binding : ActivityMainBinding
    companion object {
        val TAG = "MainActivity"
    }
    var category = 15
    var difficulty = "easy"
    var amount = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val categoryDropdown : Spinner = findViewById(R.id.spinner_main_categories)
        ArrayAdapter.createFromResource(this, R.array.categories_array,
            android.R.layout.simple_spinner_item).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            categoryDropdown.adapter = adapter
        }
        categoryDropdown.setSelection(0, false)
        categoryDropdown.onItemSelectedListener = this

        val difficultyDropdown : Spinner = findViewById(R.id.spinner_main_difficulty)
        ArrayAdapter.createFromResource(this, R.array.difficulties_array,
            android.R.layout.simple_spinner_item).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            difficultyDropdown.adapter = adapter
        }
        difficultyDropdown.setSelection(0, false)
        difficultyDropdown.onItemSelectedListener = this

        binding.buttonMainStartQuiz.setOnClickListener {
            getQuestions()
        }
    }

    fun getQuestions() {
        if (binding.editTextNumberMainQuestionNumber.text.isEmpty()
            || binding.editTextNumberMainQuestionNumber.text.toString() == "0") {
            Toast.makeText(this@MainActivity, "Input a non-zero number of questions!", Toast.LENGTH_SHORT).show()
            return
        }
        amount = binding.editTextNumberMainQuestionNumber.text.toString().toInt()

        val triviaApi = RetrofitHelper.getInstance().create(TriviaService::class.java)
        val triviaCall = triviaApi.getData(amount, category, difficulty)
        triviaCall.enqueue(object : Callback<JsonInfo> {
            override fun onResponse(
                call: Call<JsonInfo>,
                response: Response<JsonInfo>
            ) {
                if (response.body()?.responseCode ?: 0 == 1) {
                    Toast.makeText(
                        this@MainActivity,
                        "We could not find a sufficient number of questions for that category and difficulty." +
                                "\n Please choose a different combination, or choose a smaller number of questions.",
                        Toast.LENGTH_LONG
                    ).show()
                    return
                } else if (response.body()?.responseCode ?: 6 != 0) { // ?: 5 gives arbitrary number
                    Toast.makeText(this@MainActivity, "An unexpected error occurred. Please try again.", Toast.LENGTH_LONG).show()
                    Log.d(TAG, "onResponse: ${response.body()?.responseCode}")
                    return
                }

                val questions = response.body()?.results as ArrayList<Question>
                for (question in questions) {
                    question.question = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        Html.fromHtml(question.question, Html.FROM_HTML_MODE_LEGACY).toString()
                    } else {
                        Html.fromHtml(question.question).toString()
                    }
                }

                if (questions.toHashSet().size != questions.size) {
                    return getQuestions()
                }

                val quizIntent = Intent(this@MainActivity, QuizActivity::class.java).apply {
                    putParcelableArrayListExtra(QuizActivity.EXTRA_QUESTIONS, questions)
                }
                startActivity(quizIntent)
            }

            override fun onFailure(call: Call<JsonInfo>, t: Throwable) {
                Log.d(TAG, "onFailure: ${t.message}")
            }

        })
    }

    override fun onItemSelected(parent: AdapterView<*>, view: View?, pos: Int, id: Long) {
        when (parent.getItemAtPosition(pos)) {
            "Video Games" -> category = 15
            "Film" -> category = 11
            "General Knowledge" -> category = 9
            "Music" -> category = 12
            "Easy" -> difficulty = "easy"
            "Medium" -> difficulty = "medium"
            "Hard" -> difficulty = "hard"
        }
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
        TODO("Not yet implemented")
    }
}