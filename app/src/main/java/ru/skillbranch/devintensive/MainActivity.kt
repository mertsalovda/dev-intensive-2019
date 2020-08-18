package ru.skillbranch.devintensive

import android.graphics.Color
import android.graphics.PorterDuff
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_main.*
import ru.skillbranch.devintensive.models.Bender

class MainActivity : AppCompatActivity() {

    private lateinit var benderImage: ImageView
    private lateinit var messageEt: EditText

    private lateinit var textTxt: TextView

    private lateinit var sendBtn: ImageButton

    private lateinit var benderObj: Bender

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        benderImage = iv_bender
        messageEt = et_message
        textTxt = tv_text
        sendBtn = iv_send

        val status = savedInstanceState?.getString(STATUS_KEY) ?: Bender.Status.NORMAL.name
        val question = savedInstanceState?.getString(QUESTION_KEY) ?: Bender.Question.NAME.name
        benderObj = Bender(Bender.Status.valueOf(status), Bender.Question.valueOf(question))

        val (r, g, b) = benderObj.status.color
        benderImage.setColorFilter(Color.rgb(r, g, b), PorterDuff.Mode.MULTIPLY)

        textTxt.text = benderObj.askQuestion()

        sendBtn.setOnClickListener {
            val (phrase, color) = benderObj.listenAnswer(messageEt.text.toString().toLowerCase())
            textTxt.text = phrase
            val (r, g, b) = color
            benderImage.setColorFilter(Color.rgb(r, g, b), PorterDuff.Mode.MULTIPLY)
        }
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)

        outState?.putString(STATUS_KEY, benderObj.status.name)
        outState?.putString(QUESTION_KEY, benderObj.question.name)
    }

    companion object {
        private const val STATUS_KEY = "STATUS"
        private const val QUESTION_KEY = "QUESTION_KEY"
    }
}