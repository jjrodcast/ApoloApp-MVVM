/*
 * *
 *  * Developed by Jorge Rodriguez on 27/07/18 11:28 PM
 *  * Copyright (c) 2018 . All rights reserved.
 *  * Last modified 27/07/18 11:28 PM
 *
 */
package pe.com.onecode.apoloapp.utilities

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.support.annotation.LayoutRes
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast

fun Activity.hideKeyboard() {
    currentFocus?.let {
        val inputManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputManager.hideSoftInputFromWindow(currentFocus.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
    }
}

fun Activity.openKeyboard() {
    val inputManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    inputManager.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, 0)
}


fun Activity.toast(message: CharSequence, duration: Int = Toast.LENGTH_SHORT) {
    runOnUiThread {
        Toast.makeText(this, message, duration).show()
    }
}

inline fun <reified T> Activity.gotToActivity(noinline params: Intent.() -> Unit) {
    val intent = Intent(this, T::class.java)
    intent.params()
    startActivity(intent)
}

fun ViewGroup.inflate(@LayoutRes layout: Int) = LayoutInflater.from(context).inflate(layout, this, false)