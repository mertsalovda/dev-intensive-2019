package ru.skillbranch.devintensive.models

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
        fun makeUser(fullName: String?): User {
            lastId++
            if (fullName == null) {
                return User(lastId.toString())
            }

            val parts = fullName.split(" ")

            val firstName = parts.getOrNull(0)
            val lastName = parts.getOrNull(1)

            return User(lastId.toString(), firstName, lastName)
        }
    }
}