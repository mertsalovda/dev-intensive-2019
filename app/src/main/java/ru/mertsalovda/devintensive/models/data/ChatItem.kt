package ru.mertsalovda.devintensive.models.data

/**
 * Представление [Chat] для отображения в пользовательском интерфейсе
 *
 * @property id индификатор чата в виде строки
 * @property avatar ссылка на аватар
 * @property initials инициалы для составления аватара без картинки
 * @property title заголовок чата
 * @property shortDescription сокращённое последнее сообщение
 * @property messageCount количество непрочитанных сообщений
 * @property lastMessageDate дата последнего сообщения в чате
 * @property isOnline флаг пользователя находящегося в сети
 * @property chatType тип чата [ChatType]
 * @property author автор последнего сообщения
 */
data class ChatItem (
    val id: String,
    val avatar: String?,
    val initials: String,
    val title: String,
    val shortDescription: String?,
    val messageCount: Int = 0,
    val lastMessageDate: String?,
    val isOnline: Boolean = false,
    val chatType : ChatType = ChatType.SINGLE,
    var author :String? = null
)