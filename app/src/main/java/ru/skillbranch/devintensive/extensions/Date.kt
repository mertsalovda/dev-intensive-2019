package ru.skillbranch.devintensive.extensions

import java.text.SimpleDateFormat
import java.util.*

/**
 * Форматирует дату в виде строки согласно переданному шаблоку или использует шаблон по умолчанию.
 *
 * @param pattern шаблон форматирования даты. По умолчанию "HH:mm:ss dd.MM.yy" локаль RU.
 * @return строку отформатированную согласно шаблона.
 *
 * Пример:
 * Date().format() //14:00:00 27.06.19
 * Date().format("HH:mm") //14:00
 */
fun Date.humanizeDiff(pattern: String = "HH:mm:ss dd.MM.yy"): String {
    val dateFormat = SimpleDateFormat(pattern)
    return dateFormat.format(this)
}