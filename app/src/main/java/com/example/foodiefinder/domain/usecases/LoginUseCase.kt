package com.example.foodiefinder.domain.usecases

import com.example.foodiefinder.data.repository.AuthRepository
import com.google.firebase.auth.FirebaseUser
import javax.inject.Inject

/**
 * A use case class for logging in a user.
 */
class LoginUseCase @Inject constructor(private val authRepository: AuthRepository) {

    /**
     * Signs in a user with the provided email and password.
     *
     * @param email The user's email address.
     * @param password The user's password.
     * @return A `Result` object containing either a `FirebaseUser` on success, or an `Exception` on failure.
     */

    suspend fun signInWithEmailAndPassword(email: String, password: String): Result<FirebaseUser?> {
        return authRepository.signInWithEmailAndPassword(email, password)
    }

}
