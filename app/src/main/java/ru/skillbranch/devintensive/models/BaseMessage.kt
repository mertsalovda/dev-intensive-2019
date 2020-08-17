package ru.skillbranch.devintensive.models

import java.util.*

abstract class BaseMessage(
    val id: String,
    val from: User?,
    val chat: Chat,
    val isIncoming: Boolean = false,
    val date: Date = Date()
) {
    /**
     * @return возвращает строку содержащюю информацию о id сообщения, имени получателя/отправителя, виде сообщения ("получил/отправил") и типе сообщения ("сообщение"/"изображение")
     */
    abstract fun formatMessage(): String

    /**
     * Abstract Factory по созданию объектов реализующих BaseMessage
     */
    companion object AbstractFactory {
        var lastId = -1

        /**
         * Метод возвращает реализацию класса BaseMessage в зависимости от передано type
         * @param from User отправитель сообщения
         * @param chat чат назначения
         * @param date дата отправки, по умочанию текущая дата
         * @param type "text" или "image" вернёт соответствующие реализации TextMessage или ImageMessage
         * @param payload полезная нагрузка сообщения
         * @return возвращает реализацию класса BaseMessage в зависимости от передано type
         */
        fun makeMessage(
            from: User?,
            chat: Chat,
            date: Date = Date(),
            type: String = "text",
            payload: Any?
        ): BaseMessage {
            lastId++

            return when (type) {
                "image" -> ImageMessage(lastId.toString(), from, chat, date = date, image = payload as String)
                else -> TextMessage(lastId.toString(), from, chat, date = date, text = payload as String)
            }
        }
    }
}