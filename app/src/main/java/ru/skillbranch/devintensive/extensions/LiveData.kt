package ru.skillbranch.devintensive.extensions

import androidx.lifecycle.MutableLiveData

/**
 * Создаёт объект [MutableLiveData] с значение по умолчаю
 *
 * @param T тип который будет содержать [MutableLiveData]
 * @param defaultValue значение по умолчанию
 * @return возвращает MutableLiveData<T>, с значением по умолчанию, если defaultValue не равен null
 */
fun <T> mutableLiveData(defaultValue: T? = null): MutableLiveData<T> {
    val data = MutableLiveData<T>()
    if (defaultValue != null) {
        data.value = defaultValue
    }
    return data
}