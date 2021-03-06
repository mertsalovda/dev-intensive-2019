package ru.mertsalovda.devintensive.viewmodels

import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.mertsalovda.devintensive.models.data.Profile
import ru.mertsalovda.devintensive.repositories.PreferencesRepository

class ProfileViewModel : ViewModel() {

    private val repository: PreferencesRepository = PreferencesRepository

    private val profileData = MutableLiveData<Profile>()
    private val appTheme = MutableLiveData<Int>()

    init {
        profileData.value = repository.getProfile()
        appTheme.value = repository.getAppTheme()
    }

    fun getProfileData(): LiveData<Profile> = profileData
    fun getAppTheme(): LiveData<Int> = appTheme

    /**
     * Сохраняет [Profile] в репоозиторий
     *
     * @param profile профайл пользователя
     */
    fun saveProfileDate(profile: Profile) {
        repository.saveProfile(profile)
        profileData.value = profile
    }

    /**
     * Переключает тему и сохраняет в репозиторий
     *
     */
    fun switchTheme() {
        if (appTheme.value == AppCompatDelegate.MODE_NIGHT_YES) {
            appTheme.value = AppCompatDelegate.MODE_NIGHT_NO
        } else {
            appTheme.value = AppCompatDelegate.MODE_NIGHT_YES
        }
        repository.saveAppTheme(appTheme.value!!)
    }
}