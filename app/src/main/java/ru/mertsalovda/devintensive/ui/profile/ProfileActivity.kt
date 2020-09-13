package ru.mertsalovda.devintensive.ui.profile

import android.content.Context
import android.content.Intent
import android.graphics.*
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_profile.*
import ru.mertsalovda.devintensive.R
import ru.mertsalovda.devintensive.models.data.Profile
import ru.mertsalovda.devintensive.ui.auth.AuthActivity
import ru.mertsalovda.devintensive.utils.Utils
import ru.mertsalovda.devintensive.viewmodels.ProfileViewModel

class ProfileActivity : AppCompatActivity() {

    companion object {
        const val IS_EDIT_MODE = "IS_EDIT_MODE"

        fun start(context: Context) {
            val intent = Intent(context, ProfileActivity::class.java)
            context.startActivity(intent)
        }
    }

    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient

    private lateinit var viewModel: ProfileViewModel
    var isEditMode = false
    lateinit var viewFields: Map<String, TextView>

    override fun onCreate(savedInstanceState: Bundle?) {
        // Переключение на тему приложения, чтобы не отображался Splash Screen
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        initToolbar()
        initViews(savedInstanceState)
        initViewModel()

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)

        auth = Firebase.auth
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_profile, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_logout -> {
                signOut()
                true
            }
            R.id.action_delete_account -> {
                revokeAccess()
                true
            }
            android.R.id.home -> {
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    /**
     * Выйти из учётной записи
     *
     */
    private fun signOut() {
        // Firebase sign out
        auth.signOut()

        // Google sign out
        googleSignInClient.signOut().addOnCompleteListener(this) {
            goToAuth()
        }
    }

    /**
     * Удаляет аккаунт из базы данных Firebase и анулирует доступ Google аккаунта
     *
     */
    private fun revokeAccess() {
        // Google revoke access
        googleSignInClient.revokeAccess().addOnCompleteListener(this) {
            Log.d("ProfileActivity", "revokeAccess ${it.isSuccessful}")
        }

        auth.currentUser!!
                .delete()
                .addOnCompleteListener {
                    Log.d("ProfileActivity", "delete ${it.isSuccessful}")
                    goToAuth()
                }
    }

    private fun goToAuth() {
        AuthActivity.start(this)
        finish()
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        outState?.putBoolean(IS_EDIT_MODE, isEditMode)
    }

    private fun initToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Профиль"
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
                "firstName" to et_first_name,
                "lastName" to et_last_name,
                "about" to et_about
        )
        showCurrentMode(isEditMode)

        // Переключает режим редактирования
        btn_edit.setOnClickListener {
            if (!isEditMode) {
                updateEditMode()
            } else if (isEditMode) {
                saveProfileInfo()
                updateEditMode()
            }
        }

        btn_switch_theme.setOnClickListener {
            viewModel.switchTheme()
        }
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
        Glide.with(this).load(auth.currentUser?.photoUrl).into(iv_avatar)
        iv_avatar.setInitials(initials)
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
                repository = ""
        ).apply {
            viewModel.saveProfileDate(this)
        }
    }
}