package com.example.anonifydemo.ui.utils

class ValidationUtil {

    companion object {
        fun isValidEmail(email: String): Boolean {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
        }

        fun isValidPassword(password: String): Boolean {
            val passwordRegex = "^(?=.*[0-9])(?=.*[a-zA-Z])(?=.*[@#$%^&+=])(?=\\S+\$).{6,}$"
            return password.matches(passwordRegex.toRegex())
        }

        fun passwordsMatch(password: String, confirmPassword: String): Boolean {
            return password == confirmPassword
        }

        fun isValidPostContent(content: String): Boolean {
            return content.isNotBlank() // Check if the content is not null, empty, or blank
        }

        fun isHashtagValid(hashtag: String, suggestionList: List<String>): Boolean {
            return suggestionList.contains(hashtag)
        }


    }
}