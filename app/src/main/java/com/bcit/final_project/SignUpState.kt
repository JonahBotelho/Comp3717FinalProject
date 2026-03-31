package com.bcit.final_project

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

class SignUpState {
    var name by mutableStateOf("")
    var email by mutableStateOf("")
    val onEmailChanged: (String) -> Unit = {
        email = it
        invalidEmail = !email.contains("@") // UI logic
    }
    var invalidEmail = false

}