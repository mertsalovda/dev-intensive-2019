package ru.mertsalovda.devintensive.ui.main

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*
import ru.mertsalovda.devintensive.R
import ru.mertsalovda.devintensive.models.data.ChatItem
import ru.mertsalovda.devintensive.ui.adapters.ChatAdapter
import ru.mertsalovda.devintensive.ui.adapters.ChatItemTouchHelperCallback
import ru.mertsalovda.devintensive.ui.archive.ArchiveActivity
import ru.mertsalovda.devintensive.ui.group.GroupActivity
import ru.mertsalovda.devintensive.viewmodels.MainViewModel


class MainActivity : AppCompatActivity() {

    private lateinit var chatAdapter: ChatAdapter
    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initToolbar()
        initViews()
        initViewModel()
    }

    private fun initToolbar() {
        setSupportActionBar(toolbar)
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

    private fun initViews() {
        val listener: (ChatItem) -> Unit = { Snackbar.make(rv_chat_list, "Click on ${it.title}", Snackbar.LENGTH_SHORT).show() }
        val archiveListener: (ChatItem) -> Unit = {
            val intent = Intent(this, ArchiveActivity::class.java)
            startActivity(intent)
        }

        chatAdapter = ChatAdapter(listener, archiveListener)
        val divider = DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        val touchCallback = ChatItemTouchHelperCallback(chatAdapter) { chat ->
            viewModel.addToArchive(chat.id)
            Snackbar.make(rv_chat_list, "Вы точно хотите добавить ${chat.title} в архив?", Snackbar.LENGTH_LONG)
                    .setAction("Нет") { viewModel.restoreFromArchive(chat.id) }
                    .show()
        }
        val touchHelper = ItemTouchHelper(touchCallback)
        touchHelper.attachToRecyclerView(rv_chat_list)

        rv_chat_list.apply {
            adapter = chatAdapter
            layoutManager = LinearLayoutManager(this@MainActivity)
            addItemDecoration(divider)
        }


        fab.setOnClickListener {
            val intent = Intent(this, GroupActivity::class.java)
            startActivity(intent)
        }
    }

    private fun initViewModel() {
        viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
        viewModel.getChatData().observe(this, Observer {
            tv_empty_list.visibility = if (it.size > 1) View.GONE else View.VISIBLE
            chatAdapter.updateData(it)
        })
    }
}
