package ru.mertsalovda.devintensive.models.data

import androidx.annotation.VisibleForTesting
import ru.mertsalovda.devintensive.extensions.shortFormat
import ru.mertsalovda.devintensive.extensions.truncate
import ru.mertsalovda.devintensive.models.BaseMessage
import ru.mertsalovda.devintensive.models.ImageMessage
import ru.mertsalovda.devintensive.models.TextMessage
import ru.mertsalovda.devintensive.utils.Utils
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
    fun unreadableMessageCount(): Int = messages.filter { it.isReadied == false }.size

    /**
     * Получить дату последнего сообщения
     *
     * @return дата последнего сообщения
     */
    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    fun lastMessageDate(): Date? {
        return if (messages.size > 0) {
            messages.last().date
        } else {
            null
        }
    }

    /**
     * Получить последнее сообщение в обрезанном виде
     *
     * @return последнее сообщение в чате в обрезанном виде
     */
    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    fun lastMessageShort(): Pair<String, String?> = when (val lastMessage = messages.lastOrNull()) {
        is TextMessage -> lastMessage.text!!.truncate(32) to lastMessage.from.firstName
        is ImageMessage -> "${lastMessage.from.firstName} - отправил фото" to lastMessage.from.firstName
        else -> "Сообщений пока нет" to ""
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
        return if (isSingle() && id.toInt() > 0 ) {
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
        } else if (!isSingle() && id.toInt() > 0 ){
            ChatItem(
                    id,
                    null,
                    getInitialsGroup(members),
                    title,
                    lastMessageShort().first,
                    unreadableMessageCount(),
                    lastMessageDate()?.shortFormat(),
                    false,
                    ChatType.GROUP,
                    lastMessageShort().second
            )
        } else {
            ChatItem(
                    id,
                    null,
                    getInitialsGroup(members),
                    title,
                    lastMessageShort().first,
                    unreadableMessageCount(),
                    lastMessageDate()?.shortFormat(),
                    false,
                    ChatType.ARCHIVE,
                    lastMessageShort().second
            )
        }
    }

    /**
     * Создаёт инициалы для группы пользователей
     *
     * @param members список пользователей
     * @return строку с инициалами или "??"
     */
    private fun getInitialsGroup(members: List<User>): String {
        if (members.isEmpty()) return "??"
        val firstUser = members.first()
        return Utils.toInitials(firstUser.firstName, firstUser.lastName) ?: "??"
    }
}

/**
 * Типы чатов
 *
 */
enum class ChatType {
    SINGLE,
    GROUP,
    ARCHIVE
}



