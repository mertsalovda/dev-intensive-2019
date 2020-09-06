package ru.skillbranch.devintensive

import android.graphics.Color
import android.graphics.PorterDuff
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_main.*
import ru.skillbranch.devintensive.extensions.hideKeyboard
import ru.skillbranch.devintensive.models.Bender

class MainActivity : AppCompatActivity() {

    lateinit var benderImage: ImageView
    lateinit var messageEt: EditText

    lateinit var textTxt: TextView

    lateinit var sendBtn: ImageButton

    lateinit var benderObj: Bender

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

        sendBtn.setOnClickListener { sendMessage() }

        messageEt.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                sendMessage()
                true
            }
            false
        }
    }

    private fun sendMessage() {
        val (phrase, color) = benderObj.listenAnswer(messageEt.text.toString().toLowerCase())
        textTxt.text = phrase
        val (r, g, b) = color
        benderImage.setColorFilter(Color.rgb(r, g, b), PorterDuff.Mode.MULTIPLY)
        (messageEt as TextView).text = ""
        this.hideKeyboard()
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