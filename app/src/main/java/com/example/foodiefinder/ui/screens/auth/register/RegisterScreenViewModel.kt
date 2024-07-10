package com.example.foodiefinder.ui.screens.auth.register

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.foodiefinder.core.widgets.utils.validators.Validator
import com.example.foodiefinder.data.model.MUser
import com.example.foodiefinder.ui.screens.auth.BaseViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

/**
 * A view model class for the registration screen.
 */
@HiltViewModel
class RegisterScreenViewModel @Inject constructor(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore,
    private val storage: FirebaseStorage
) : BaseViewModel() {

    private val _loading = MutableLiveData(false)
    val loading: LiveData<Boolean> = _loading

    /**
     * Validates the registration form fields.
     */
    fun validateRegistrationForm(name: String, email: String, password: String): Boolean {
        val errors = Validator.validateRegistrationFields(name, email, password)

        _nameError.value = errors["name"]
        _emailError.value = errors["email"]
        _passwordError.value = errors["password"]

        return errors.isEmpty()
    }

    /**
     * Creates a new user with the provided email and password.
     */
    fun createUserWithEmailPassword(
        name: String,
        email: String,
        password: String,
        avatarUri: Uri?,
        home: () -> Unit
    ) = viewModelScope.launch {

        if (!validateRegistrationForm(name, email, password)) {

            return@launch
        }

        _loading.value = true

        try {

            val authResult = auth.createUserWithEmailAndPassword(email, password).await()
            val userId = authResult.user?.uid
            val displayName = email.split('@').firstOrNull() ?: "User"

            userId?.let { uid ->
                avatarUri?.let {
                    uploadImageToStorage(uid, it) { imageUrl ->
                        createUser(displayName, name, email, uid, imageUrl)
                        home()
                    }
                } ?: run {
                    createUser(displayName, name, email, uid, null)
                    home()
                }
            }
        } catch (e: FirebaseAuthException) {
            _emailError.value = e.message
        } catch (e: Exception) {
            _emailError.value = "Failed to register. Please try again."
        } finally {
            _loading.value = false
        }
    }


    /**
     * Uploads an image to Firebase Storage and returns the download URL.
     */
    private fun uploadImageToStorage(userId: String, uri: Uri, onComplete: (String) -> Unit) {
        val storageRef = storage.reference.child("avatars/$userId")
        val uploadTask = storageRef.putFile(uri)

        uploadTask.continueWithTask { task ->
            if (!task.isSuccessful) {
                task.exception?.let { throw it }
            }
            storageRef.downloadUrl
        }.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                task.result?.let { uri ->
                    onComplete(uri.toString())
                }
            } else {
                task.exception?.let { throw it }
            }
        }
    }

    /**
     * Creates a new user in Firestore with the provided user data.
     */
    private fun createUser(
        displayName: String?,
        name: String,
        email: String,
        uid: String,
        avatarUrl: String?
    ) {
        val user = MUser(
            userId = uid,
            displayName = displayName,
            name = name,
            email = email,
            avatarUrl = avatarUrl
        )
        firestore.collection("users").document(uid).set(user)
    }

}

