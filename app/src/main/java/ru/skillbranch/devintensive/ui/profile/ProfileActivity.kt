package ru.skillbranch.devintensive.ui.profile

import android.graphics.*
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import kotlinx.android.synthetic.main.activity_profile.*
import ru.skillbranch.devintensive.R
import ru.skillbranch.devintensive.models.Profile
import ru.skillbranch.devintensive.ui.custom.AvatarInitialsDrawable
import ru.skillbranch.devintensive.utils.Utils
import ru.skillbranch.devintensive.viewmodels.ProfileViewModel

class ProfileActivity : AppCompatActivity() {

    companion object {
        const val IS_EDIT_MODE = "IS_EDIT_MODE"
    }

    private lateinit var viewModel: ProfileViewModel
    var isEditMode = false
    lateinit var viewFields: Map<String, TextView>

    override fun onCreate(savedInstanceState: Bundle?) {
        // Переключение на тему приложения, чтобы не отображался Splash Screen
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        initViews(savedInstanceState)
        initViewModel()
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        outState?.putBoolean(IS_EDIT_MODE, isEditMode)
    }

    /**
     * Инициализирует View-элементы экрана
     *
     * @param savedInstanceState
     */
    private fun initViews(savedInstanceState: Bundle?) {
        isEditMode = savedInstanceState?.getBoolean(IS_EDIT_MODE, false) ?: false
        viewFields = mapOf(
                "nickName" to tv_nick_name,
                "rank" to tv_rank,
                "firstName" to et_first_name,
                "lastName" to et_last_name,
                "about" to et_about,
                "repository" to et_repository,
                "rating" to tv_rating,
                "respect" to tv_respect
        )
        showCurrentMode(isEditMode)

        // Переключает режим редактирования
        btn_edit.setOnClickListener {
            if (!isEditMode) {
                updateEditMode()
            } else if (isEditMode && wr_repository.error == null) {
                saveProfileInfo()
                updateEditMode()
            }
        }

        btn_switch_theme.setOnClickListener {
            viewModel.switchTheme()
        }

        et_repository.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(text: Editable?) {
                if (Utils.validGithubURL(text.toString())) {
                    wr_repository.error = ""
                }
            }

            override fun beforeTextChanged(text: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {
                if (Utils.validGithubURL(text.toString())) {
                    wr_repository.error = ""
                } else {
                    wr_repository.error = "Невалидный адрес репозитория"
                }
            }
        })
    }

    /**
     * Переключает режим редактирования и запускает обновление интерфейса.
     *
     */
    private fun updateEditMode() {
        isEditMode = !isEditMode
        showCurrentMode(isEditMode)
    }

    /**
     * Иницилизирует [ProfileViewModel] для текущей активити и подписывается на [Profile]
     *
     */
    private fun initViewModel() {
        viewModel = ViewModelProviders.of(this).get(ProfileViewModel::class.java)
        viewModel.getProfileData().observe(this, Observer { updateUI(it) })
        viewModel.getAppTheme().observe(this, Observer { updateTheme(it) })
    }

    /**
     * Обновляет интерфейс.
     * Прозодит по [viewFields] и заполняет текст
     *
     * @param profile профиль пользователя
     */
    private fun updateUI(profile: Profile) {
        profile.toMap().also {
            for ((k, v) in viewFields) {
                v.text = it[k].toString()
            }
        }
        updateAvatar(profile)
    }

    /**
     * Обновляет аватар. Если имя и фамилия profile пустые, то подставиться аватар по умолчанию,
     * иначе будет загружен аватар с инициалами [AvatarInitialsDrawable].
     *
     * @param profile объект [Profile]
     */
    private fun updateAvatar(profile: Profile) {
        val firstName = Utils.toInitials(profile.firstName, "")
        val lastName = Utils.toInitials(profile.lastName, "")
        val initialsFirstName = Utils.transliteration(firstName ?: "")
        val initialsLastName = Utils.transliteration(lastName ?: "")
        val initials = "$initialsFirstName$initialsLastName"
        val avatar = if (initials == "") {
            resources.getDrawable(R.drawable.avatar_default, theme)
        } else {
            resources.getDrawable(R.drawable.avatar_initials, theme)

        }
        val textSize = if (initials.length >= 4) 70f else 80f
        val avatarInitialsDrawable = AvatarInitialsDrawable(avatar, initials, textSize)
        iv_avatar.setImageDrawable(avatarInitialsDrawable)
    }

    /**
     * Обновляет тему. [Activity] будет пересоздана.
     *
     * @param mode режим AppCompatDelegate.MODE_NIGHT_YES или AppCompatDelegate.MODE_NIGHT_NO
     */
    private fun updateTheme(mode: Int) {
        delegate.setLocalNightMode(mode)
    }

    /**
     * Метод настраивает отображение полей ввода информации о пользователе
     *
     * @param isEdit true - режим редактирования включён, false - выключен
     */
    private fun showCurrentMode(isEdit: Boolean) {
        // Из viewFields получаю только значения с ключами "firstName", "lastName", "about", "repository"
        val info = viewFields.filter { setOf("firstName", "lastName", "about", "repository").contains(it.key) }
        for ((_, v) in info) {
            v as EditText
            v.isFocusable = isEdit
            v.isEnabled = isEdit
            v.isFocusableInTouchMode = isEdit
            v.background.alpha = if (isEdit) 255 else 0
        }

        ic_eye.visibility = if (isEdit) View.GONE else View.VISIBLE
        wr_about.isCounterEnabled = isEdit

        // Меняю иконку btn_edit и переключаю фон
        btn_edit.apply {
            val filter: ColorFilter? = if (isEdit) {
                PorterDuffColorFilter(
                        resources.getColor(R.color.color_accent, theme),
                        PorterDuff.Mode.SRC_IN
                )
            } else {
                null
            }

            val icon = if (isEdit) {
                resources.getDrawable(R.drawable.ic_save_black_24dp, theme)
            } else {
                resources.getDrawable(R.drawable.ic_edit_black_24dp, theme)
            }
            background.colorFilter = filter
            setImageDrawable(icon)
        }
    }

    /**
     * Сохраняет [Profile] в [PreferencesRepository]
     *
     */
    private fun saveProfileInfo() {
        Profile(
                firstName = et_first_name.text.toString(),
                lastName = et_last_name.text.toString(),
                about = et_about.text.toString(),
                repository = et_repository.text.toString()
        ).apply {
            viewModel.saveProfileDate(this)
        }
    }
}
