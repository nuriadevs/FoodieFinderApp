package com.example.foodiefinder.ui.screens.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.foodiefinder.core.widgets.utils.validators.Validator
import com.example.foodiefinder.core.widgets.utils.validators.Validator.Companion.isValidEmail
import com.example.foodiefinder.core.widgets.utils.validators.Validator.Companion.isValidPassword

/**
 *  A base view model class that provides common functionality for view models.
 */
abstract class BaseViewModel : ViewModel() {

    protected val _nameError = MutableLiveData<String?>(null)
    val nameError: LiveData<String?> = _nameError


    protected val _emailError = MutableLiveData<String?>(null)
    val emailError: LiveData<String?> = _emailError

    protected val _passwordError = MutableLiveData<String?>(null)
    val passwordError: LiveData<String?> = _passwordError

    protected val _nameValid = MutableLiveData(false)
    val nameValid: LiveData<Boolean> = _nameValid


    protected val _emailValid = MutableLiveData(false)
    val emailValid: LiveData<Boolean> = _emailValid

    protected val _passwordValid = MutableLiveData(false)
    val passwordValid: LiveData<Boolean> = _passwordValid

    protected val _errorMessage = MutableLiveData<String?>(null)
    val errorMessage: LiveData<String?> = _errorMessage

    fun onNameChange(name: String) {
        val (isValid, error) = Validator.isValidName(name)
        _nameValid.value = isValid
        _nameError.value = error
    }


    fun onEmailChange(email: String) {
        _emailValid.value = isValidEmail(email)
        if (_emailValid.value == true) {
            _emailError.value = null
        } else {
            _emailError.value = "Invalid email format."
        }
    }

    fun onPasswordChange(password: String) {
        _passwordValid.value = isValidPassword(password)
        if (_passwordValid.value == true) {
            _passwordError.value = null
        } else {
            _passwordError.value = "Password must be at least 6 characters"
        }
    }

    fun clearErrorMessages() {
        _nameError.value = null
        _emailError.value = null
        _passwordError.value = null
    }

}
