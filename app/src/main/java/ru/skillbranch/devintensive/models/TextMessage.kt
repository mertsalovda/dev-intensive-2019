package ru.skillbranch.devintensive.models

import ru.skillbranch.devintensive.models.data.Chat
import ru.skillbranch.devintensive.models.data.User
import java.util.*

/**
 * Класс описывающий сообщение с текстом реализует [BaseMessage]
 *
 * @property id идентификатор сообщения
 * @property from отправитель сообщения
 * @property chat место назначения сообщения
 * @property isIncoming флаг входящего сообщения
 * @property date дата отправки/получения
 * @property isReadied флаг прочтения сообщения
 * @property text текст сообщения
 */
class TextMessage(
        id: String,
        from: User,
        chat: Chat,
        isIncoming: Boolean = false,
        date: Date = Date(),
        isReadied: Boolean = false,
        var text: String?
) : BaseMessage(id, from, chat, isIncoming, date, isReadied)

