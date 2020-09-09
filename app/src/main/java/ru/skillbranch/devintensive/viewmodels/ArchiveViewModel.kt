package ru.skillbranch.devintensive.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import ru.skillbranch.devintensive.extensions.mutableLiveData
import ru.skillbranch.devintensive.models.data.ChatItem
import ru.skillbranch.devintensive.repositories.ChatRepository

/**
 * ViewModel для общего списка чатов [ArchiveActivity]
 *
 */
class ArchiveViewModel : ViewModel() {
    // Поисковый запрос пользователей
    private var query = mutableLiveData("")
    private val chatRepository = ChatRepository

    // Список чатов
    private val chats = mutableLiveData(loadCats())

    /**
     * Получить список чатов из репозитория
     *
     * @return список чатов [ChatItem]
     */
    private fun loadCats(): List<ChatItem> {
        return chatRepository.loadChats().value!!
                .filter { it.isArchived }
                .map { it.toChatItem() }
                .sortedBy { it.id.toInt() }
    }

    /**
     * Получить список пользователй соответствующих поисковому запросу query
     *
     * @return список пользователей удовлетворяющих запрос query
     */
    fun getChatData(): LiveData<List<ChatItem>> {
        // MediatorLiveData объединяет два источника LiveData
        val result = MediatorLiveData<List<ChatItem>>()

        // Функция фильтрации пользователй в соответствии с запросом query
        val filterF = {
            val queryStr = query.value!!
            val chatsValue = chats.value!!

            result.value = if (queryStr.isEmpty()) chatsValue
            else chatsValue.filter { it.title.contains(queryStr, true) }
        }
        // Объединение LiveData в MediatorLiveData
        result.addSource(chats) { filterF.invoke() }
        result.addSource(query) { filterF.invoke() }

        return result
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

    /**
     * Обработчик поискового запроста. Установит новое значение для LiveData query
     *
     * @param text новый текст запроса
     */
    fun handleSearchQuery(text: String?) {
        query.value = text
    }
}