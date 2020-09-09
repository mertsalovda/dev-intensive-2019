package ru.skillbranch.devintensive.viewmodels

import androidx.lifecycle.*
import ru.skillbranch.devintensive.extensions.mutableLiveData
import ru.skillbranch.devintensive.models.data.ChatItem
import ru.skillbranch.devintensive.repositories.ChatRepository

/**
 * ViewModel для общего списка чатов [MainActivity]
 *
 */
class MainViewModel : ViewModel() {
    // Поисковый запрос пользователей
    private var query = mutableLiveData("")
    private val chatRepository = ChatRepository

    /**
     * Список чатов. Подписан на изменение чатов в репозитории
     */
    private val chats = Transformations.map(chatRepository.loadChats()) { chats ->
        return@map chats
                .filter { !it.isArchived }
                .map { it.toChatItem() }
                .sortedBy { it.id.toInt() }
    } as MutableLiveData

    /**
     * Получить список чатов соответствующих поисковому запросу query
     *
     * @return список чатов удовлетворяющих запрос query
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
    fun addToArchive(chatId: String) {
        val chat = chatRepository.find(chatId)
        chat ?: return
        chatRepository.update(chat.copy(isArchived = true))
    }

    /**
     * Вернуть чат из архива
     *
     * @param chatId идентификатор чата для возвращения из архив
     */
    fun restoreFromArchive(chatId: String) {
        val chat = chatRepository.find(chatId)
        chat ?: return
        chatRepository.update(chat.copy(isArchived = false))
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