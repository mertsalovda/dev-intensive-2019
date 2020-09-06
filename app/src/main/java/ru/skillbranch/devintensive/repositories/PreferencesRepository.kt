package ru.skillbranch.devintensive.repositories

import android.content.SharedPreferences
import android.preference.PreferenceManager
import androidx.appcompat.app.AppCompatDelegate
import ru.skillbranch.devintensive.App
import ru.skillbranch.devintensive.ui.profile.Profile

object PreferencesRepository {

    private const val FIRST_NAME = "FIRST_NAME"
    private const val LAST_NAME = "LAST_NAME"
    private const val ABOUT = "ABOUT"
    private const val REPOSITORY = "REPOSITORY"
    private const val RATING = "RATING"
    private const val RESPECT = "RESPECT"
    private const val APP_THEME = "APP_THEME"


    private val pref: SharedPreferences by lazy {
        val ctx = App.applicationContext()
        PreferenceManager.getDefaultSharedPreferences(ctx)
    }

    /**
     * Достаёт из репозитоиря данные о пользователе
     *
     * @return [Profile]
     */
    fun getProfile(): Profile {
        pref.apply {
            return Profile(
                    firstName = getString(FIRST_NAME, ""),
                    lastName = getString(LAST_NAME, ""),
                    about = getString(ABOUT, ""),
                    repository = getString(REPOSITORY, ""),
                    rating = getInt(RATING, 0),
                    respect = getInt(RESPECT, 0)
            )
        }
    }

    /**
     * Сохраняет тему в репозитории
     *
     * @param theme AppCompatDelegate.MODE_NIGHT_YES или AppCompatDelegate.MODE_NIGHT_NO
     */
    fun saveAppTheme(theme: Int) {
        putValue(APP_THEME to theme)
    }

    fun saveProfile(profile: Profile) {
        profile.apply {
            putValue(FIRST_NAME to firstName)
            putValue(LAST_NAME to lastName)
            putValue(ABOUT to about)
            putValue(REPOSITORY to repository)
            putValue(RATING to rating)
            putValue(RESPECT to respect)
        }
    }

    /**
     * Сохраняет примитивные данные в репозитории
     *
     * @param pair Пара значений KEY [String] и VALUE [Any]
     * @exception error при попытке добавить непримитивные данные бросает [IllegalStateException]
     */
    fun putValue(pair: Pair<String, Any?>) {
        pref.edit().apply {
            val key = pair.first
            val value = pair.second

            when (value) {
                is String -> putString(key, value)
                is Int -> putInt(key, value)
                is Boolean -> putBoolean(key, value)
                is Float -> putFloat(key, value)
                is Long -> putLong(key, value)
                else -> error("Only primitives types can be stored in storage")
            }

            apply()
        }
    }

    /**
     * Возвращает тему сохранённую в репозитории
     *
     */
    fun getAppTheme(): Int = pref.getInt(APP_THEME, AppCompatDelegate.MODE_NIGHT_NO)
}
