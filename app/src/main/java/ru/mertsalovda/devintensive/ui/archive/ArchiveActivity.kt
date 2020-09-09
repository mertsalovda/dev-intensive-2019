package ru.mertsalovda.devintensive.ui.archive

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_archive.*
import ru.mertsalovda.devintensive.R
import ru.mertsalovda.devintensive.models.data.ChatItem
import ru.mertsalovda.devintensive.ui.adapters.ChatAdapter
import ru.mertsalovda.devintensive.ui.adapters.ChatItemTouchHelperCallback
import ru.mertsalovda.devintensive.ui.adapters.TouchType
import ru.mertsalovda.devintensive.viewmodels.ArchiveViewModel

class ArchiveActivity : AppCompatActivity() {
    private lateinit var chatAdapter: ChatAdapter
    private lateinit var viewModel: ArchiveViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_archive)
        initToolbar()
        initViews()
        initViewModel()
    }

    private fun initToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.apply {
            title = resources.getString(R.string.title_archive_chats)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_search, menu)
        val searchItem = menu?.findItem(R.id.action_search)
        val searchView = searchItem?.actionView as SearchView
        searchView.queryHint = "Введите название чата"
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                viewModel.handleSearchQuery(query)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                viewModel.handleSearchQuery(newText)
                return true
            }

        })
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
        val listener: (ChatItem) -> Unit = { Snackbar.make(rv_chat_list, "Click on ${it.title}", Snackbar.LENGTH_SHORT).show() }
        val archiveListener: (ChatItem) -> Unit = {}

        chatAdapter = ChatAdapter(listener, archiveListener)
        val divider = DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        val touchCallback = ChatItemTouchHelperCallback(chatAdapter, TouchType.UNARCHIVE) { chat ->
            viewModel.removeFromArchive(chat.id)
            Snackbar.make(rv_chat_list, "Вы точно хотите убрать ${chat.title.trim()} из архива?", Snackbar.LENGTH_LONG)
                    .setAction("Нет") { viewModel.restoreToArchive(chat.id) }
                    .show()
        }
        val touchHelper = ItemTouchHelper(touchCallback)
        touchHelper.attachToRecyclerView(rv_chat_list)

        rv_chat_list.apply {
            adapter = chatAdapter
            layoutManager = LinearLayoutManager(this@ArchiveActivity)
            addItemDecoration(divider)
        }
    }

    private fun initViewModel() {
        viewModel = ViewModelProviders.of(this).get(ArchiveViewModel::class.java)
        viewModel.getChatData().observe(this, Observer {
            tv_empty_list.visibility = if(it.isNotEmpty()) View.GONE else View.VISIBLE
            chatAdapter.updateData(it)
        })
    }
}