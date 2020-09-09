package ru.mertsalovda.devintensive.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import ru.mertsalovda.devintensive.extensions.mutableLiveData
import ru.mertsalovda.devintensive.models.data.UserItem
import ru.mertsalovda.devintensive.repositories.GroupRepository

/**
 * ViewModel для выбора участников группового чата в [GroupActivity]
 *
 */
class GroupViewModel : ViewModel() {

    // Поисковый запрос пользователей
    private var query = mutableLiveData("")
    private val groupRepository = GroupRepository
    // Все пользователи
    private val userItems = mutableLiveData(loadUsers())
    // Список выделенных пользователй
    private val selectedItems = Transformations.map(userItems) { users -> users.filter { it.isSelected } }

    /**
     * Получить список пользователй соответствующих поисковому запросу query
     *
     * @return список пользователей удовлетворяющих запрос query
     */
    fun getUsersData(): LiveData<List<UserItem>> {
        // MediatorLiveData объединяет два источника LiveData
        val result = MediatorLiveData<List<UserItem>>()

        // Функция фильтрации пользователй в соответствии с запросом query
        val filterF = {
            val queryStr = query.value!!
            val users = userItems.value!!

            result.value = if (queryStr.isEmpty()) users
            else users.filter { it.fullName.contains(queryStr, true) }
        }
        // Объединение LiveData в MediatorLiveData
        result.addSource(userItems) { filterF.invoke() }
        result.addSource(query) { filterF.invoke() }

        return result
    }

    /**
     * Получить список выделенных пользователей
     *
     * @return список выделенных пользователй
     */
    fun getSelectedData(): LiveData<List<UserItem>> = selectedItems

    /**
     * Обработчик выделения пользователя. Устанавливает флаг выделения isSelected для пользователя
     *
     * @param userId идентификатор выделенного пользователя
     */
    fun handleSelectedItem(userId: String) {
        userItems.value = userItems.value!!.map {
            if (it.id == userId) it.copy(isSelected = !it.isSelected)
            else it
        }
    }

    /**
     * Обработчик удаления [Chip]. Удаляет чип и устанавливает флак выделения isSelected = false
     *
     * @param userId идентификатор пользователя, для которого будут снято выделение
     */
    fun handleRemoveChip(userId: String) {
        userItems.value = userItems.value!!.map {
            if (it.id == userId) it.copy(isSelected = false)
            else it
        }
    }

    /**
     * Обработчик поискового запроста. Установит новое значение для LiveData query
     *
     * @param text новый текст запроса
     */
    fun handleSearchQuery(text: String?) {
        query.value = text
    }

    /**
     * Обработчик создания группового чата пользоватлей.
     * Создать групповой чат из выделенных пользователей
     */
    fun handleCreateGroup() {
        groupRepository.createChat(selectedItems.value!!)
    }

    /**
     * Получить список пользователй из репозитория
     *
     * @return список пользователей [UserItem]
     */
    private fun loadUsers(): List<UserItem> = groupRepository.loadUsers().map { it.toUserItem() }
}