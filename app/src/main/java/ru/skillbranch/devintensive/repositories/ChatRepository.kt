package ru.skillbranch.devintensive.repositories

import androidx.lifecycle.MutableLiveData
import ru.skillbranch.devintensive.data.managers.CacheManager
import ru.skillbranch.devintensive.models.data.Chat

/**
 * Предоставляет доступ к списку [Chat]
 */
object ChatRepository {

    private val chats = CacheManager.loadChats()

    /**
     * Предоставляет список [Chat] обёрнутый в [MutableLiveData]
     *
     */
    fun loadChats(): MutableLiveData<List<Chat>> {
        return chats
    }

    /**
     * Добавить чат в репозиторий
     *
     * @param chat будет добавлен в репозиторий
     */
    fun update(chat: Chat) {
        val copy = chats.value!!.toMutableList()
        val ind = chats.value!!.indexOfFirst { it.id == chat.id }
        if(ind == -1) return
        copy[ind] = chat
        chats.value = copy
    }

    /**
     * Получить чат по идентификатору
     *
     * @param chatId идентификатор чата
     * @return найденый чат или null
     */
    fun find(chatId: String): Chat? {
        val ind = chats.value!!.indexOfFirst { it.id == chatId }
        return chats.value!!.getOrNull(ind)

    }
}