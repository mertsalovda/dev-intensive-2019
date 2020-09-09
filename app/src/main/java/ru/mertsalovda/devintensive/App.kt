package ru.mertsalovda.devintensive

import android.app.Application
import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import ru.mertsalovda.devintensive.repositories.PreferencesRepository

class App : Application() {

    companion object {
        private var instance: App? = null
        /**
         * @return [Context] приложения
         */
        fun applicationContext(): Context {
            return instance!!.applicationContext
        }
    }

    init {
        instance = this
    }


    override fun onCreate() {
        super.onCreate()
        // Установка сохранённой темы
        PreferencesRepository.getAppTheme().also {
            AppCompatDelegate.setDefaultNightMode(it)
        }
    }
}