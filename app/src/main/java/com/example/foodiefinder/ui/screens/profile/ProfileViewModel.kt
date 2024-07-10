package com.example.foodiefinder.ui.screens.profile

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.foodiefinder.core.exception.DataOrException
import com.example.foodiefinder.data.model.MUser
import com.example.foodiefinder.data.repository.UserRepository
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * A view model class for the profile screen.
 */
@HiltViewModel
class ProfileViewModel @Inject constructor(private val userRepository: UserRepository) :
    ViewModel() {

    private val firebaseAuth = FirebaseAuth.getInstance()


    private val _user = MutableStateFlow<MUser?>(null)
    val user: StateFlow<MUser?> = _user

    private val _updateResult =
        MutableStateFlow<DataOrException<MUser, Boolean, Exception>>(DataOrException())
    val updateResult: StateFlow<DataOrException<MUser, Boolean, Exception>> = _updateResult


    init {
        loadUser()
    }

    /**
     * Loads the user data from the repository.
     */
    private fun loadUser() {
        val currentUser = firebaseAuth.currentUser
        currentUser?.uid?.let { userId ->
            viewModelScope.launch {
                _user.value = null
                val result = userRepository.getUserData(userId)
                result.data?.let {
                    _user.value = MUser(
                        userId = it["userId"] as? String,
                        name = it["name"] as? String,
                        displayName = it["displayName"] as? String,
                        email = it["email"] as? String,
                        avatarUrl = it["avatarUrl"] as? String
                    )
                }
            }
        }
    }


    /**
     * Updates the user data in the repository.
     */
    fun updateUserWithImage(user: MUser, imageUri: Uri?) {
        viewModelScope.launch {
            _updateResult.value = DataOrException(loading = true)

            try {
                val newImageUrl = imageUri?.let {
                    userRepository.uploadImage(it, user.avatarUrl)
                } ?: user.avatarUrl

                val updatedUser = user.copy(avatarUrl = newImageUrl)
                val result = userRepository.updateUser(updatedUser)
                _updateResult.value = result

                if (result.data != null) {
                    _user.value = result.data
                }
            } catch (e: Exception) {
                _updateResult.value = DataOrException(e = e)
            }
        }
    }

}