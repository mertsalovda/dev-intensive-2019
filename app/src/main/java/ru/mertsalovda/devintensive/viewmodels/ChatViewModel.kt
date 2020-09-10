package ru.mertsalovda.devintensive.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.mertsalovda.devintensive.models.BaseMessage
import ru.mertsalovda.devintensive.models.data.Chat
import ru.mertsalovda.devintensive.repositories.ChatRepository

class ChatViewModel : ViewModel() {

    var chatId: String = "0"
    private val chatRepository = ChatRepository

    private val chatLiveData = MutableLiveData<Chat>()

    private val _messages = MutableLiveData<List<BaseMessage>>()
    val messages: MutableLiveData<List<BaseMessage>> = _messages

    fun getMessageData(): MutableLiveData<List<BaseMessage>> {
        return messages
    }

    fun getChat() : MutableLiveData<Chat> {
        return chatLiveData
    }

    fun loadData() {
        val messageList = chatRepository.loadChats().value!!.first { it.id == chatId }.messages
        messages.postValue(messageList)
    }
}
