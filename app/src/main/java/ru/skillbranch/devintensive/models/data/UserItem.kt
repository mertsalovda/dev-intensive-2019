package ru.skillbranch.devintensive.models.data

/**
 * Представление класса [User] для пользовательских интерфейсов
 *
 * @property id идентификатор пльзователя в виде строки
 * @property fullName Имя и Фамилия пользователя
 * @property initials инициалы пользователя
 * @property avatar ссылка на аватар пользователя
 * @property lastActivity TODO
 * @property isSelected флаг выделения пользователя в списке
 * @property isOnline флаг наличия пользователя в сети
 */
data class UserItem (
    val id: String,
    val fullName: String,
    val initials : String?,
    val avatar: String?,
    var lastActivity:String,
    var isSelected : Boolean = false,
    var isOnline: Boolean = false
)