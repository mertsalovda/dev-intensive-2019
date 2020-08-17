package ru.skillbranch.devintensive.models

import ru.skillbranch.devintensive.utils.Utils
import java.util.*

data class User(
    val id: String,
    var firstName: String?,
    var lastName: String?,
    var avatar: String?,
    var rating: Int = 0,
    var respect: Int = 0,
    var lastVisit: Date? = Date(),
    var isOnline: Boolean = false
) {
    constructor(id: String, firstName: String?, lastName: String?) : this(
        id = id,
        firstName = firstName,
        lastName = lastName,
        avatar = null
    )

    constructor(id: String) : this(id, "John", "Doe")

    companion object Factory {
        private var lastId = -1

        /**
         * Создаёт объект User, использую входную строку для формирования имени и фамилии
         *
         * @param fullName строка вида "Имя Фамилия"
         * @return User
         */
        fun makeUser(fullName: String?): User {
            lastId++

            val (firstName, lastName) = Utils.parseFullName(fullName)
            return User(lastId.toString(), firstName, lastName)
        }
    }

    /**
     * Класс Builder для класса User.
     *
     */
    class Builder {
        private lateinit var id: String
        private var firstName: String? = null
        private var lastName: String? = null
        private var avatar: String? = null
        private var rating: Int = 0
        private var respect: Int = 0
        private var lastVisit: Date? = Date()
        private var isOnline: Boolean = false

        fun id(id: String): Builder  {
            this.id = id
            return this
        }

        fun firstName(firstName: String?): Builder {
            this.firstName = firstName
            return this
        }

        fun lastName(lastName: String?): Builder  {
            this.lastName = lastName
            return this
        }

        fun avatar(avatar: String?): Builder  {
            this.avatar = avatar
            return this
        }

        fun rating(rating: Int = 0): Builder  {
            this.rating = rating
            return this
        }

        fun respect(respect: Int = 0): Builder  {
            this.respect = respect
            return this
        }

        fun lastVisit(lastVisit: Date? = Date()): Builder  {
            this.lastVisit = lastVisit
            return this
        }

        fun isOnline(isOnline: Boolean = false): Builder  {
            this.isOnline = isOnline
            return this
        }

        fun build(): User {
            return User(id, firstName, lastName, avatar, rating, respect, lastVisit, isOnline)
        }
    }
}