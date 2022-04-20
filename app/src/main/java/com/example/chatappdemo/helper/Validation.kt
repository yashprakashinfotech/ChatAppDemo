package com.example.chatappdemo.helper

import android.text.TextUtils
import android.util.Patterns
import java.util.regex.Pattern

class Validation {

    companion object{

        fun isValidPasswordFormat(password: String): Boolean {
            val passwordREGEX = Pattern.compile("^" +
                    "(?=.*[0-9])" +         //at least 1 digit
                    "(?=.*[a-z])" +         //at least 1 lower case letter
                    "(?=.*[A-Z])" +         //at least 1 upper case letter
                    "(?=.*[a-zA-Z])" +      //any letter
                    "(?=.*[@#$%^&+=])" +    //at least 1 special character
                    "(?=\\S+$)" +           //no white spaces
                    ".{8,}" +               //at least 8 characters
                    "$")
            return passwordREGEX.matcher(password).matches()
        }

        fun isValidEmailFormat(email: String): Boolean {

            return !TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches()
        }

        fun isValidSpaceFormat(space: String): Boolean {
            val spaceREGEX = Pattern.compile("^[\\s]+\$")   // only space regex
            return spaceREGEX.matcher(space).matches()
        }

        fun isValidLatterFormat(latter: String): Boolean {
            val latterREGEX = Pattern.compile("^[a-zA-Z\\s]+\$")   // only Alphabets allow in this regex
            return latterREGEX.matcher(latter).matches()
        }
    }
}