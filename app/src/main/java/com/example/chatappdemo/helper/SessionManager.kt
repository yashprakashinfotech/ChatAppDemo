package com.example.chatappdemo.helper

import android.content.Context


class SessionManager(context: Context) {

    var privateMode = 0
    val prefFile = "PrakashJobApp"

    private var pref = context.getSharedPreferences(prefFile, privateMode)
    private var prefEditor = pref.edit()

    fun putString(key_name: String, value: String) {
        prefEditor.putString(key_name, value)
        prefEditor.apply()
    }

    fun getString(Key_name: String): String? {
        return pref.getString(Key_name, "defaultString")
    }

    fun putBoolean(key_name: String, isLoggedIn: Boolean) {
        prefEditor.putBoolean(key_name, isLoggedIn)
        prefEditor.apply()
    }

    fun isLoggedIn(): Boolean {
        return pref.getBoolean(KeyClass.KEY_USER_LOGIN, false)
    }

}