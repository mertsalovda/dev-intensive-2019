package ru.mertsalovda.devintensive.models

import ru.mertsalovda.devintensive.models.data.Chat
import ru.mertsalovda.devintensive.models.data.User
import java.util.*

/**
 * Базовый класс сообщений
 *
 * @property id идентификатор сообщения
 * @property from отправитель сообщения
 * @property chat место назначения сообщения
 * @property isIncoming флаг входящего сообщения
 * @property date дата отправки/получения
 * @property isReadied флаг прочтения сообщения
 *
 * @see ImageMessage
 * @see TextMessage
 */
abstract class BaseMessage(
    val id: String,
    val from: User,
    val chat: Chat,
    val isIncoming: Boolean = true,
    val date: Date = Date(),
    var isReadied: Boolean = false

)