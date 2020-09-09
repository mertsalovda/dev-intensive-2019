package ru.skillbranch.devintensive.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import ru.skillbranch.devintensive.models.data.ChatItem
import ru.skillbranch.devintensive.repositories.ChatRepository

/**
 * ViewModel для общего списка чатов [ArchiveActivity]
 *
 */
class ArchiveViewModel : ViewModel() {
    private val chatRepository = ChatRepository

    /**
     * Список чатов. Подписан на изменение чатов в репозитории
     */
    private val chats = Transformations.map(chatRepository.loadChats()) { chats ->
        return@map chats
                .filter { it.isArchived }
                .map { it.toChatItem() }
                .sortedBy { it.id.toInt() }
    }

    /**
     * Получить список чатов
     *
     * @return список чатов
     */
    fun getChatData(): LiveData<List<ChatItem>> {
        return chats
    }

    /**
     * Добавить чат в архив
     *
     * @param chatId идентификатор чата для отправки в архива
     */
    fun removeFromArchive(chatId: String) {
        val chat = chatRepository.find(chatId)
        chat ?: return
        chatRepository.update(chat.copy(isArchived = false))
    }

    /**
     * Вернуть чат в архив
     *
     * @param chatId идентификатор чата для возвращения в архив
     */
    fun restoreToArchive(chatId: String) {
        val chat = chatRepository.find(chatId)
        chat ?: return
        chatRepository.update(chat.copy(isArchived = true))

    }
}