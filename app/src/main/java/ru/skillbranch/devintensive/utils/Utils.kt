package ru.skillbranch.devintensive.utils

object Utils {

    /**
     * Преобразует строку вида "FirstName LastName" в объект Pair<String?, String?>
     *
     * @param fullName строка вида "FirstName LastName"
     * @return пара значений firstName и lastName
     *
     * Utils.parseFullName(null) //null null
     * Utils.parseFullName("") //null null
     * Utils.parseFullName(" ") //null null
     * Utils.parseFullName("John") //John null
     * Utils.parseFullName("John Snow") //John Snow
     */
    fun parseFullName(fullName: String?): Pair<String?, String?> {

        if (fullName.isNullOrEmpty() || fullName == " ") {
            return null to null
        }
        val parts = fullName.split(" ")
        val firstName = parts.getOrNull(0)
        val lastName = parts.getOrNull(1)

        return firstName to lastName
    }
}