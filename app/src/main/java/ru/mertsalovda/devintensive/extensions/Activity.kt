package ru.mertsalovda.devintensive.extensions

import android.app.Activity
import android.content.Context
import android.graphics.Rect
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager


/**
 * Закрывает программную клавиатуру
 */
fun Activity.hideKeyboard() {
    val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(findViewById<View>(android.R.id.content).windowToken, 0)
}

/**
 * Проверяется открыта ли программная клавиатура
 *
 * @return true если программная клавиатура открыта, false если закрыта
 */
fun Activity.isKeyboardOpen(): Boolean {
    var result = false
    val activityRootView = findViewById<ViewGroup>(android.R.id.content).getChildAt(0)
    val rect = Rect()
    activityRootView.getWindowVisibleDisplayFrame(rect)
    val diffHeight = activityRootView.height - rect.height()
    if (diffHeight > 0.25 * activityRootView.height) {
        result = true
    }
    return result
}
/**
 * Проверяется закрыта ли программная клавиатура
 *
 * @return true если программная клавиатура закрыта, false если открыта
 */
fun Activity.isKeyboardClosed(): Boolean {
    var result = false
    val activityRootView = findViewById<ViewGroup>(android.R.id.content).getChildAt(0)
    val rect = Rect()
    activityRootView.getWindowVisibleDisplayFrame(rect)
    val diffHeight = activityRootView.height - rect.height()
    if (diffHeight < 0.25 * activityRootView.height) {
        result = true
    }
    return result
}