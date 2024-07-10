package com.example.foodiefinder.ui.screens.auth.login


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.foodiefinder.core.widgets.utils.validators.Validator
import com.example.foodiefinder.domain.usecases.LoginUseCase
import com.example.foodiefinder.ui.screens.auth.BaseViewModel
import com.example.foodiefinder.ui.screens.auth.AuthViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


/**
 * A view model class for the login screen.
 */
@HiltViewModel
class LoginScreenViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase
) : BaseViewModel() {

    private val _loading = MutableLiveData(false)
    val loading: LiveData<Boolean> = _loading

    /**
     * Validates the login form fields.
     */
    fun validateLoginForm(email: String, password: String): Boolean {
        val errors = Validator.validateLoginFields(email, password)

        _emailError.value = errors["email"]
        _passwordError.value = errors["password"]

        return errors.isEmpty()
    }

    /**
     * Signs in a user with the provided email and password.
     */
    fun signInWithEmailAndPassword(email: String, password: String, authViewModel: AuthViewModel, home: () -> Unit) {
        if (!validateLoginForm(email, password)) {
            return
        }

        _loading.value = true

        viewModelScope.launch {
            try {
                val result = loginUseCase.signInWithEmailAndPassword(email, password)

                result.onSuccess {
                    authViewModel.authenticate(email, password)
                    home()  // Redirigir al home
                }.onFailure { e ->
                    _passwordError.value = "Failed. Please check your email and password."
                }
            } catch (e: Exception) {
                // Manejar excepciones generales
                _emailError.value = "Failed to sign in. Please check your email and password."
            } finally {
                _loading.value = false
            }
        }
    }


}
