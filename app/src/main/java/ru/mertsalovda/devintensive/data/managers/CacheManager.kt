package ru.mertsalovda.devintensive.data.managers

import androidx.lifecycle.MutableLiveData
import ru.mertsalovda.devintensive.extensions.mutableLiveData
import ru.mertsalovda.devintensive.models.data.Chat
import ru.mertsalovda.devintensive.models.data.User
import ru.mertsalovda.devintensive.utils.DataGenerator

object CacheManager {
    private val chats = mutableLiveData(DataGenerator.stabChats)
    private val users = mutableLiveData(DataGenerator.stabUsers)

    /**
     * Предоставляет список [Chat] обёрнутый в [MutableLiveData]
     */
    fun loadChats(): MutableLiveData<List<Chat>> {
        return chats
    }

    /**
     * Осуществляет поик среди всех [User] и возвращает отфильтрованный список [User]
     *
     * @param ids список id пользователей
     * @return список пользователь, которые имеют id из списка ids
     */
    fun findUserByIds(ids: List<String>): List<User> {
        return users.value!!.filter { ids.contains(it.id) }
    }

    /**
     * Получить свободный id для чата
     *
     * @return свободный id в виде строки равный длине списка чатов
     */
    fun nextChatId(): String {
        return "${chats.value!!.size}"
    }

    /**
     * Добавить чат в репозиторий
     *
     * @param chat объект [Chat], который будет добавлен в репозиторий
     */
    fun insertChat(chat: Chat){
        val copy = chats.value!!.toMutableList()
        copy.add(chat)
        chats.value = copy
    }
}