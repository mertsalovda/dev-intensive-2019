package ru.skillbranch.devintensive.utils

import java.lang.StringBuilder

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

    /**
     * Возвращающий строку из латинских символов.
     *
     * @param payload строка, которую надо траслетилировать.
     * @param divider разделитель в итоговой строке, по умолчанию " ".
     * @return возвращающий строку из латинских символов
     *
     * Utils.transliteration("Женя Стереотипов") //Zhenya Stereotipov
     * Utils.transliteration("Amazing Петр", "_") //Amazing_Petr
     */
    fun transliteration(payload: String, divider: String = " "): String {
        val sb = StringBuilder()
        for (char in payload) {
            val symbol = symbols[char.toString().toLowerCase()] ?: char.toString()
            sb.append(symbol)
        }
        if (payload == "") return ""
        val split = sb.toString().split(payload, " ")
        return if (split.size == 1) {
            split[0].capitalize()
        } else {
            split.joinToString(separator = divider) { it.capitalize() }
        }
    }
    /*   fun transliteration(payload: String, divider: String = " "): String {
           val dict = mapOf(
                   'а' to "а",
                   'б' to "b",
                   'в' to "v",
                   'г' to "g",
                   'д' to "d",
                   'е' to "e",
                   'ё' to "e",
                   'ж' to "zh",
                   'з' to "z",
                   'и' to "i",
                   'й' to "i",
                   'к' to "k",
                   'л' to "l",
                   'м' to "m",
                   'н' to "n",
                   'о' to "o",
                   'п' to "p",
                   'р' to "r",
                   'с' to "s",
                   'т' to "t",
                   'у' to 'u',
                   'ф' to "f",
                   'х' to "h",
                   'ц' to "c",
                   'ч' to "ch",
                   'ш' to "sh",
                   'щ' to "sh'",
                   'ъ' to "",
                   'ы' to "i",
                   'ь' to "",
                   'э' to "e",
                   'ю' to "yu",
                   'я' to "ya"
           )
           var res: String = ""

           for (i in 0..payload.length - 1) {
               if (dict.containsKey(payload[i].toLowerCase())) {
                   when {
                       i == 0 -> res += dict[payload[i].toLowerCase()].toString().toUpperCase()
                       payload[i - 1].toString() == " " -> res += dict[payload[i].toLowerCase()].toString()
                               .toUpperCase()
                       else -> res += dict[payload[i].toLowerCase()].toString()
                   }
               } else {
                   if (payload[i].toString() == " ")
                       res += divider
                   else
                       res += payload[i]
               }
           }
           return res
       }*/

    /**
     * Словарь траслитирации
     */
    private val symbols = hashMapOf<String, String>(
            "а" to "a",
            "б" to "b",
            "в" to "v",
            "г" to "g",
            "д" to "d",
            "е" to "e",
            "ё" to "e",
            "ж" to "zh",
            "з" to "z",
            "и" to "i",
            "й" to "i",
            "к" to "k",
            "л" to "l",
            "м" to "m",
            "н" to "n",
            "о" to "o",
            "п" to "p",
            "р" to "r",
            "с" to "s",
            "т" to "t",
            "у" to "u",
            "ф" to "f",
            "х" to "h",
            "ц" to "c",
            "ч" to "ch",
            "ш" to "sh",
            "щ" to "sh'",
            "ъ" to "",
            "ы" to "i",
            "ь" to "",
            "э" to "e",
            "ю" to "yu",
            "я" to "ya"
    )
}