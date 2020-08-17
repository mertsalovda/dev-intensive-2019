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

/**
 * Добавляющий или вычитающий значение переданное первым аргументом
 *
 * @param value значение на которое будет изменено значение даты.
 * @param unit единицах измерения enum TimeUnits [SECOND, MINUTE, HOUR, DAY].
 * @return модифицированный экземпляр Date.
 */
fun Date.add(value: Int, unit: TimeUnits): Date {
    var time = this.time
    time += when (unit) {
        TimeUnits.SECOND -> value * 1000
        TimeUnits.MINUTE -> value * 60 * 1000
        TimeUnits.HOUR -> value * 60 * 60 * 1000
        TimeUnits.DAY -> value * 24 * 60 * 60 * 1000
    }
    return Date(time)
}

enum class TimeUnits {
    SECOND, MINUTE, HOUR, DAY
}