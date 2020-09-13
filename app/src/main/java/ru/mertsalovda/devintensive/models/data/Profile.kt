package ru.mertsalovda.devintensive.models.data

import ru.mertsalovda.devintensive.utils.Utils

data class Profile(
        val firstName: String?,
        val lastName: String?,
        val about: String?,
        val repository: String?,
        val rating: Int = 0,
        val respect: Int = 0
) {

    var nickName: String = Utils.transliteration("$firstName $lastName".trim())
    var rank: String = "Junior Android Developer"

    /**
     * Представляет объект [Profile] в виде [Map]
     *
     * @return Map<K, V>
     */
    fun toMap(): Map<String, Any?> = mapOf(
            "nickName" to nickName,
            "rank" to rank,
            "firstName" to firstName,
            "lastName" to lastName,
            "about" to about,
            "repository" to repository,
            "rating" to rating,
            "respect" to respect
    )
}