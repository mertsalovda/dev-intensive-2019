package ru.skillbranch.devintensive.models

import ru.skillbranch.devintensive.extensions.humanizeDiff
import java.util.*

class ImageMessage(
    id: String,
    from: User?,
    chat: Chat,
    isIncoming: Boolean = false,
    date: Date = Date(),
    var image: String?
) : BaseMessage(id, from, chat, isIncoming, date) {

    /**
     * @return возвращает строку содержащюю информацию о id сообщения, имени отправителя,
     * виде сообщения ("получил/отправил") и типе сообщения ("изображение")
     */
    override fun formatMessage() = "id:$id, ${from?.firstName} ${if (isIncoming) "получил" else "отправил"} изображение \"$image\" ${date.humanizeDiff()}"
}