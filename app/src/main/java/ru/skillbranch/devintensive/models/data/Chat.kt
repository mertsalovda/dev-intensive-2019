package ru.skillbranch.devintensive.models.data

import androidx.annotation.VisibleForTesting
import ru.skillbranch.devintensive.extensions.shortFormat
import ru.skillbranch.devintensive.models.BaseMessage
import ru.skillbranch.devintensive.utils.Utils
import java.util.*

/**
 * Класс данных представляющий чат
 *
 * @property id чата в виде строки
 * @property title заголовок чата
 * @property members участники чата
 * @property messages сообщения чата
 * @property isArchived флаг, обозначающий перемещён ли чат в архив
 */
data class Chat(
        val id: String,
        val title: String,
        val members: List<User> = listOf(),
        var messages: MutableList<BaseMessage> = mutableListOf(),
        var isArchived: Boolean = false
) {
    /**
     * Получить число непрочитанных сообщений в чате
     *
     * @return число непрочитанных сообщений
     */
    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    fun unreadableMessageCount(): Int {
        //TODO implement me
        return 0
    }

    /**
     * Получить дату последнего сообщения
     *
     * @return дата последнего сообщения
     */
    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    fun lastMessageDate(): Date? {
        //TODO implement me
        return Date()
    }

    /**
     * Получить последнее сообщение в обрезанном виде
     *
     * @return последнее сообщение в чате в обрезанном виде
     */
    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    fun lastMessageShort(): Pair<String, String?> = when(val lastMessage = messages.lastOrNull()){
       //TODO implement me
        else -> "" to ""
    }

    /**
     * Флаг определяющий одиночный или груповой чат
     */
    private fun isSingle(): Boolean = members.size == 1

    /**
     * Преобразовать [Chat] в его представления [ChatItem] для списков
     *
     * @return [ChatItem] соответствующий групповому или одиночному чату
     */
    fun toChatItem(): ChatItem {
        return if (isSingle()) {
            val user = members.first()
            ChatItem(
                id,
                user.avatar,
                Utils.toInitials(user.firstName, user.lastName) ?: "??",
                "${user.firstName ?: ""} ${user.lastName ?: ""}",
                lastMessageShort().first,
                unreadableMessageCount(),
                lastMessageDate()?.shortFormat(),
                user.isOnline
            )
        } else {
            ChatItem(
                id,
                null,
                "",
                title,
                lastMessageShort().first,
                unreadableMessageCount(),
                lastMessageDate()?.shortFormat(),
                false,
                ChatType.GROUP,
                lastMessageShort().second
            )
        }
    }
}

/**
 * Типы чатов
 *
 */
enum class ChatType{
    SINGLE,
    GROUP,
    ARCHIVE
}



