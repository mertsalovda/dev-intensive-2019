package ru.skillbranch.devintensive.repositories

import ru.skillbranch.devintensive.data.managers.CacheManager
import ru.skillbranch.devintensive.models.data.Chat
import ru.skillbranch.devintensive.models.data.User
import ru.skillbranch.devintensive.models.data.UserItem
import ru.skillbranch.devintensive.utils.DataGenerator

/**
 * Репозиторий пользователей [User]
 */
object GroupRepository {
    /**
     * Заполнить и получить список пользователей
     *
     * @return список пользователей
     */
    fun loadUsers(): List<User> = DataGenerator.stabUsers

    /**
     * Создаты чат [Chat] из списка пользователей [UserItem]
     *
     * @param items список пользователй
     */
    fun createChat(items: List<UserItem>) {
        val ids = items.map { it.id }
        val users = CacheManager.findUserByIds(ids)
        val title = users.map { it.firstName }.joinToString(", ")
        val chat = Chat(CacheManager.nextChatId(), title, users)
        CacheManager.insertChat(chat)
    }
}