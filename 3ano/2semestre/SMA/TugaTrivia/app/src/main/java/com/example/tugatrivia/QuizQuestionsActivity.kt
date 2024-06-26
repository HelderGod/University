package com.example.tugatrivia

import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.util.Collections

class QuizQuestionsActivity : AppCompatActivity(), View.OnClickListener {

    private var mCurrentPosition: Int = 1
    private var mQuestionsList: ArrayList<Question>? = null
    private var mSelectedOptionPosition: Int = 0
    private var mUserName: String? = null
    private var mCorrectAnswers: Int = 0

    private var progressBar: ProgressBar? = null
    private var tvProgress: TextView? = null
    private var tvQuestion: TextView? = null
    private var ivImage: ImageView? = null
    private var tvtema: TextView? = null

    private var tvOptionOne: TextView? = null
    private var tvOptionTwo: TextView? = null
    private var tvOptionThree: TextView? = null
    private var tvOptionFour: TextView? = null
    private var btnSubmit: Button? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_quiz_questions)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        mUserName = intent.getStringExtra(Constants.USER_NAME)
        val selectedDifficulty = intent.getStringExtra(Constants.SELECTED_DIFFICULTY)

        mQuestionsList = when (selectedDifficulty) {
            "easy" -> Constants.getEasyQuestions()
            "hard" -> Constants.getHardQuestions()
            else -> Constants.getEasyQuestions()
        }
        Collections.shuffle(mQuestionsList)

        progressBar = findViewById(R.id.progressBar)
        tvProgress = findViewById(R.id.tv_progress)
        ivImage = findViewById(R.id.iv_image)
        tvQuestion = findViewById(R.id.tv_question)
        tvOptionOne = findViewById(R.id.tv_option_one)
        tvOptionTwo = findViewById(R.id.tv_option_two)
        tvOptionThree = findViewById(R.id.tv_option_three)
        tvOptionFour = findViewById(R.id.tv_option_four)
        btnSubmit = findViewById(R.id.btn_submit)
        tvtema = findViewById(R.id.tv_tema)

        tvOptionOne?.setOnClickListener(this)
        tvOptionTwo?.setOnClickListener(this)
        tvOptionThree?.setOnClickListener(this)
        tvOptionFour?.setOnClickListener(this)
        btnSubmit?.setOnClickListener(this)

        setQuestion()
        defaultOptionsView()
    }


    private fun setQuestion() {
        defaultOptionsView()
        val question: Question = mQuestionsList!![mCurrentPosition - 1]
        ivImage?.setImageResource(question.image)
        progressBar?.progress = mCurrentPosition
        tvProgress?.text = "$mCurrentPosition/${progressBar?.max}"
        tvtema?.text = question.tema
        tvQuestion?.text = question.question
        tvOptionOne?.text = question.optionOne
        tvOptionTwo?.text = question.optionTwo
        tvOptionThree?.text = question.optionThree
        tvOptionFour?.text = question.optionFour

        if (mCurrentPosition == mQuestionsList!!.size) {
            btnSubmit?.text = "TERMINAR"
        } else {
            btnSubmit?.text = "PRÓXIMO"
        }
    }

    private fun defaultOptionsView() {
        val options = ArrayList<TextView>()
        tvOptionOne?.let {
            options.add(0, it)
        }
        tvOptionTwo?.let {
            options.add(1, it)
        }
        tvOptionThree?.let {
            options.add(2, it)
        }
        tvOptionFour?.let {
            options.add(3, it)
        }
        for (option in options) {
            option.setTextColor(Color.parseColor("#6d2709"))
            option.typeface = Typeface.DEFAULT
            option.background = ContextCompat.getDrawable(
                this,
                R.drawable.default_option_border_bg // R -> resources
            )
        }
    }

    private fun selectOptionView(tv: TextView, selectedOptionNum: Int) {
        defaultOptionsView()
        mSelectedOptionPosition = selectedOptionNum

        tv.setTextColor(Color.parseColor("#6d2709"))
        tv.setTypeface(tv.typeface, Typeface.BOLD)
        tv.background = ContextCompat.getDrawable(
            this,
            R.drawable.selected_option_border_bg
        )

        // Check answer and update view
        val question = mQuestionsList?.get(mCurrentPosition - 1)
        if (question!!.correctAnswer != mSelectedOptionPosition) {
            answerView(mSelectedOptionPosition, R.drawable.wrong_option_border_bg)
        } else {
            mCorrectAnswers++
        }
        answerView(question.correctAnswer, R.drawable.correct_option_border_bg)

        // Pause briefly before moving to next question
        tv.postDelayed({
            mCurrentPosition++
            if (mCurrentPosition <= mQuestionsList!!.size) {
                setQuestion()
            } else {
                val intent = Intent(this, ResultActivity::class.java)
                intent.putExtra(Constants.USER_NAME, mUserName)
                intent.putExtra(Constants.CORRECT_ANSWERS, mCorrectAnswers)
                intent.putExtra(Constants.TOTAL_QUESTIONS, mQuestionsList?.size)
                startActivity(intent)
                finish()
            }
        }, 1000)
    }


    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.tv_option_one -> {
                tvOptionOne?.let {
                    selectOptionView(it, 1)
                }
            }

            R.id.tv_option_two -> {
                tvOptionTwo?.let {
                    selectOptionView(it, 2)
                }
            }

            R.id.tv_option_three -> {
                tvOptionThree?.let {
                    selectOptionView(it, 3)
                }
            }

            R.id.tv_option_four -> {
                tvOptionFour?.let {
                    selectOptionView(it, 4)
                }
            }

            R.id.btn_submit -> {
                if (mSelectedOptionPosition == 0) {
                    val question = mQuestionsList?.get(mCurrentPosition - 1)
                    if (question != null) {
                        answerView(question.correctAnswer, R.drawable.correct_option_border_bg)
                    }
                } else {
                    val question = mQuestionsList?.get(mCurrentPosition - 1)
                    if (question != null) {
                        mCorrectAnswers++

                        answerView(question.correctAnswer, R.drawable.correct_option_border_bg)
                    }
                    mSelectedOptionPosition = 0
                }

                btnSubmit?.isEnabled = false
                btnSubmit?.postDelayed({
                    mCurrentPosition++
                    if (mCurrentPosition <= mQuestionsList!!.size) {
                        setQuestion()
                    } else {
                        val intent = Intent(this, ResultActivity::class.java)
                        intent.putExtra(Constants.USER_NAME, mUserName)
                        intent.putExtra(Constants.CORRECT_ANSWERS, mCorrectAnswers)
                        intent.putExtra(Constants.TOTAL_QUESTIONS, mQuestionsList?.size)
                        startActivity(intent)
                        finish()
                    }
                    btnSubmit?.isEnabled = true
                }, 1000)
            }
        }
    }

    private fun answerView(answer: Int, drawablesView: Int) {
        when (answer) {
            1 -> {
                tvOptionOne?.background = ContextCompat.getDrawable(
                    this@QuizQuestionsActivity,
                    drawablesView
                )
            }

            2 -> {
                tvOptionTwo?.background = ContextCompat.getDrawable(
                    this@QuizQuestionsActivity,
                    drawablesView
                )
            }

            3 -> {
                tvOptionThree?.background = ContextCompat.getDrawable(
                    this@QuizQuestionsActivity,
                    drawablesView
                )
            }

            4 -> {
                tvOptionFour?.background = ContextCompat.getDrawable(
                    this@QuizQuestionsActivity,
                    drawablesView
                )
            }
        }
    }
}