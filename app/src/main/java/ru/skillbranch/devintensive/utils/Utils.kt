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

    /**
     * Возвращающий строку с первыми буквами имени и фамилии в верхнем регистре
     *
     * @param firstName имя пользователя (null, пустуя строка)
     * @param lastName фамилия пользователя (null, пустуя строка)
     * @return возвращающий строку с первыми буквами имени и фамилии в верхнем регистре
     *
     * Пример:
     * Utils.toInitials("john", "doe") //JD
     * Utils.toInitials(" ", "doe") //D
     * Utils.toInitials("John", null) //J
     * Utils.toInitials(null, "doe") //D
     * Utils.toInitials(null, " ") //null
     * Utils.toInitials(" ", null) //null
     * Utils.toInitials(null, null) //null
     * Utils.toInitials(" ", "") //null
     */
    fun toInitials(firstName: String?, lastName: String?): String? {
        if (firstName == null && lastName == null) return null

        var one: String? = ""
        var two: String? = ""

        if (firstName != null && lastName == null) {
            if (firstName.trim().isNotEmpty()) {
                one = firstName[0].toUpperCase().toString()
            } else {
                return null
            }
        }

        if (firstName == null && lastName != null) {
            if (lastName.trim().isNotEmpty()) {
                two = lastName[0].toUpperCase().toString()
            } else {
                return null
            }
        }

        if (firstName != null && lastName != null) {
            if (firstName.trim().isEmpty() && lastName.trim().isEmpty()) return null

            if (firstName.trim().isNotEmpty()) {
                one = firstName[0].toUpperCase().toString()
            }
            if (lastName.isNotEmpty()) {
                two = lastName[0].toUpperCase().toString()
            }
        }
        return "$one$two"
    }

}