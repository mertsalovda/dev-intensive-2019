package ru.mertsalovda.devintensive.extensions

import java.util.regex.Pattern

/**
 * Усекает строку до указанного числа символов. Если строка была учечена, то добавляется
 * заполнитель "...". Лишние пробелы в конце отрезаются
 *
 * @param value Длина строки, после которой ставится запонитель "..."
 * @return модифицировання строка.
 */
fun String.truncate(value: Int = 16): String {
    var string = this.trim()
    val filling = if (string.length < value) "" else "..."
    if (string.length >= value) {
        string = string.substring(0 until value).trim()
    }
    return "$string$filling"
}

/**
 * Удаляет HTML-теги из текста и лишние пробелы.
 *
 * @return строку без HTML-тегов и одним пробелом между словами.
 */
fun String.stripHtml(): String {
    val htmlTegPattern = Pattern.compile("<.+?>");
    val spacePattern = Pattern.compile(" +");

    val htmlTegMatcher = htmlTegPattern.matcher(this)
    var result = htmlTegMatcher.replaceAll("").trim()

    val spaceMatcher = spacePattern.matcher(result)
    return spaceMatcher.replaceAll(" ").trim()
}