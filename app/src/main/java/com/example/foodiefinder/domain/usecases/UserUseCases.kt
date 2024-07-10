package com.example.foodiefinder.domain.usecases

import android.net.Uri
import com.example.foodiefinder.core.exception.DataOrException
import com.example.foodiefinder.data.model.MUser
import com.example.foodiefinder.data.repository.UserRepository
import javax.inject.Inject

/**
 * A use case class for interacting with the user repository.
 */
class UserUseCases @Inject constructor(private val userRepository: UserRepository) {


    suspend fun getUserData(uid: String?): DataOrException<Map<String, Any>, Boolean, Exception> {
        return userRepository.getUserData(uid)
    }

    suspend fun updateUser(user: MUser): DataOrException<MUser, Boolean, Exception> {
        return userRepository.updateUser(user)
    }

    suspend fun uploadImage(imageUri: Uri, previousImageUrl: String?): String {
        return userRepository.uploadImage(imageUri, previousImageUrl)
    }

    suspend fun deleteImage(imageUrl: String) {
        userRepository.deleteImage(imageUrl)
    }

    suspend fun getImageUrl(imagePath: String): String {
        return userRepository.getImageUrl(imagePath)
    }
}