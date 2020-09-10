package ru.mertsalovda.devintensive.ui.chat

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_chat.*
import ru.mertsalovda.devintensive.R
import ru.mertsalovda.devintensive.ui.adapters.MessageAdapter
import ru.mertsalovda.devintensive.viewmodels.ChatViewModel

class ChatActivity : AppCompatActivity() {

    companion object {
        const val CHAT_ID = "CHAT_ID"
    }
    private lateinit var messageAdapter: MessageAdapter
    private lateinit var viewModel: ChatViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        val chatID = intent.getStringExtra(CHAT_ID) ?: "0"

        initToolbar()
        initViews()
        initViewModel(chatID)
    }

    private fun initToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.apply {
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return if (item?.itemId == android.R.id.home) {
            finish()
            overridePendingTransition(R.anim.idle, R.anim.bottom_down)
            true
        } else {
            super.onOptionsItemSelected(item)
        }
    }

    private fun initViews() {
        messageAdapter = MessageAdapter()

        rv_message_list.apply {
            adapter = messageAdapter
            layoutManager = LinearLayoutManager(this@ChatActivity)
        }
    }

    private fun initViewModel(chatID: String) {
        viewModel = ViewModelProviders.of(this).get(ChatViewModel::class.java)
        viewModel.chatId = chatID
        viewModel.loadData()
        viewModel.getMessageData().observe(this, Observer {
            messageAdapter.updateData(it)
        })

        viewModel.getChat().observe(this, Observer {
            supportActionBar?.title = it.title
        })
    }
}
