package ru.skillbranch.devintensive.extensions

fun String.truncate(value: Int = 16): String {
    var string = this.trim()
    val filling = if (string.length < value) "" else "..."
    if (string.length >= value) {
        string = string.substring(0 until value).trim()
    }
    return "${string}$filling"
}