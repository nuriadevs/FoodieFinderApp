package com.example.foodiefinder.ui.screens.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.foodiefinder.data.repository.AuthRepository
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * A view model class for authentication-related operations.
 */
@HiltViewModel
class AuthViewModel @Inject constructor( private val authRepository: AuthRepository) : ViewModel() {
    private val _isAuthenticated = MutableLiveData(false)
    val isAuthenticated: LiveData<Boolean> get() = _isAuthenticated

    /**
     * Authenticates a user with the provided email and password.
     */
    fun authenticate(email: String, password: String) = viewModelScope.launch {
        val result = authRepository.signInWithEmailAndPassword(email, password)
        _isAuthenticated.value = result.isSuccess
    }

    /**
     * Logs out the current user.
     */
    fun logout() {
        FirebaseAuth.getInstance().signOut()
        _isAuthenticated.value = false
    }

}

