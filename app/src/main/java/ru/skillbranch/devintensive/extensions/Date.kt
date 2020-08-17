package ru.skillbranch.devintensive.extensions

import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.abs

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
fun Date.format(pattern: String = "HH:mm:ss dd.MM.yy"): String {
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
        TimeUnits.SECOND -> value.toLong() * 1000
        TimeUnits.MINUTE -> value.toLong() * 60 * 1000
        TimeUnits.HOUR -> value.toLong() * 60 * 60 * 1000
        TimeUnits.DAY -> value.toLong() * 24 * 60 * 60 * 1000
    }
    return Date(time)
}

/**
 * Форматирует вывода да в понятную человеку разницу между текущей датой и переданной датой
 *
 * @return строка указывающая на сколько дата отличается от текущей даты
 */
fun Date.humanizeDiff(): String {
    val currentTime = Date().time
    val time = this.time
    var diff = (currentTime - time) / 1000
    val isFuture = diff < 0
    diff = abs(diff)
    val future = if (isFuture) "через " else ""
    val past = if (!isFuture) "назад" else ""
    return when (diff) {
        in 0..1 -> "только что"
        in 1..45 -> "${future}несколько секунд $past"
        in 45..75 -> "${future}минуту $past"
        in 75..(45 * 60) -> "$future${diff / 60} ${getMinutesWithSuffix(diff/60)} $past"
        in (45 * 60)..(75 * 60) -> "${future}час $past"
        in (75 * 60)..(22 * 3600) -> "$future${diff / 3600} ${getHoursWithSuffix(diff / 3600)} $past"
        in (22 * 3600)..(26 * 3600) -> "${future}день $past"
        in (26 * 3600)..(360 * 3600 * 24) -> "$future${diff / 3600 / 24} ${getDaysWithSuffix(diff / 3600 / 24)} $past"
        else -> "более ${if (isFuture) "чем через " else ""}год${if (!isFuture) "а" else ""} $past"
    }
}

/**
 * Возвращает правильное сколонение слова "минуты"
 *
 * @param min колличество минут
 * @return строку с правильным сколонением слова "минуты"
 */
private fun getSecondsWithSuffix(sec: Long): String = when {
    sec in 2..4 || (sec % 10 in 2..4) && sec !in 12..14 -> "секунды"
    sec % 10 == 1L && sec != 11L -> "секунда"
    else -> "секунду"
}
/**
 * Возвращает правильное сколонение слова "минуты"
 *
 * @param min колличество минут
 * @return строку с правильным сколонением слова "минуты"
 */
private fun getMinutesWithSuffix(min: Long): String = when {
    min in 2..4 || min%10 in 2..4 && min !in 12..14 -> "минуты"
    min != 11L && min%10 == 1L -> "минуту"
    else -> "минут"
}

/**
 * Возвращает правильное сколонение слова "часы"
 *
 * @param hour колличество часов
 * @return строку с правильным сколонением слова "часы"
 */
private fun getHoursWithSuffix(hour: Long): String = when {
    hour in 2..4 || (hour%10 in 2..4) && hour !in 12..14-> "часа"
    hour%10 == 1L && hour != 11L -> "час"
    else -> "часов"
}
/**
 * Возвращает правильное сколонение слова "дни"
 *
 * @param days колличество дней
 * @return строку с правильным сколонением слова "дни"
 */
private fun getDaysWithSuffix(days: Long): String = when {
    days in 2..4 || (days%10 in 2..4) && days !in 12..14-> "дня"
    days%10 == 1L && days != 11L -> "день"
    else -> "дней"
}

enum class TimeUnits {
    SECOND {
        /**
         * Возвращает правильное сколонение слова "секунды"
         *
         * @param value секунд
         * @return строку с value + правильным сколонением слова "секунды"
         */
        override fun plural(value: Int): String = "$value ${getSecondsWithSuffix(value.toLong())}"
    },
    MINUTE {
        /**
         * Возвращает правильное сколонение слова "минуты"
         *
         * @param value колличество минут
         * @return строку с value + правильным сколонением слова "минуты"
         */
        override fun plural(value: Int): String = "$value ${getMinutesWithSuffix(value.toLong())}"
    },
    HOUR {
        /**
         * Возвращает правильное сколонение слова "часы"
         *
         * @param value колличество часов
         * @return строку с value + правильным сколонением слова "часы"
         */
        override fun plural(value: Int): String = "$value ${getHoursWithSuffix(value.toLong())}"
    },
    DAY {
        /**
         * Возвращает правильное сколонение слова "дни"
         *
         * @param value колличество дней
         * @return строку с value + правильным сколонением слова "дни"
         */
        override fun plural(value: Int): String = "$value ${getDaysWithSuffix(value.toLong())}"
    };

    abstract fun plural(value: Int): String
}