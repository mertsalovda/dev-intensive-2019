package ru.mertsalovda.devintensive.models

import ru.mertsalovda.devintensive.models.data.Chat
import ru.mertsalovda.devintensive.models.data.User
import java.util.*

/**
 * Класс описывающий сообщение с изображением реализует [BaseMessage]
 *
 * @property id идентификатор сообщения
 * @property from отправитель сообщения
 * @property chat место назначения сообщения
 * @property isIncoming флаг входящего сообщения
 * @property date дата отправки/получения
 * @property isReadied флаг прочтения сообщения
 * @property image изображение
 */
class ImageMessage(
        id: String,
        from: User,
        chat: Chat,
        isIncoming: Boolean = false,
        date: Date = Date(),
        isReadied: Boolean = false,
        var image: String
) : BaseMessage(id, from, chat, isIncoming, date, isReadied)