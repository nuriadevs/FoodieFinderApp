package com.example.foodiefinder.data.repository


import android.content.ContentValues.TAG
import android.net.Uri
import android.util.Log
import com.example.foodiefinder.core.exception.DataOrException
import com.example.foodiefinder.data.model.MUser
import com.google.firebase.Firebase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.storage

import kotlinx.coroutines.tasks.await
import java.util.UUID
import javax.inject.Inject


/**
 * A repository class for Firestore database operations.
 */
class UserRepository  @Inject constructor(private val firestore: FirebaseFirestore){

    private val db = FirebaseFirestore.getInstance()
    private val storage = Firebase.storage


    /**
     * Retrieves user data based on the user ID.
     *
     * @param uid the user ID.
     * @return a DataOrException object containing user data as a Map<String, Any> on success,
     *         or an Exception on failure.
     */

    suspend fun getUserData(uid: String?): DataOrException<Map<String, Any>, Boolean, Exception> {
        val dataOrException = DataOrException<Map<String, Any>, Boolean, Exception>()
        if (uid != null) {
            try {
                dataOrException.loading = true
                val querySnapshot = db.collection("users")
                    .whereEqualTo("userId", uid)
                    .get()
                    .await()

                Log.d(TAG, "getUserData:," + querySnapshot.documents.toString())

                if (!querySnapshot.isEmpty) {
                    val document = querySnapshot.documents[0]
                    dataOrException.data = document.data ?: emptyMap()
                } else {
                    dataOrException.e = Exception("No such document")
                }
            } catch (e: Exception) {
                dataOrException.e = e
            } finally {
                dataOrException.loading = false
            }
        } else {
            dataOrException.e = Exception("currentUserUid is null")
        }
        return dataOrException
    }

    /**
     * Updates user data in Firestore.
     *
     * @param user the user object containing updated data.
     * @return a DataOrException object containing the updated user object on success,
     *         or an Exception on failure.
     */

    suspend fun updateUser(user: MUser): DataOrException<MUser, Boolean, Exception> {
        val dataOrException = DataOrException<MUser, Boolean, Exception>()
        try {
            dataOrException.loading = true
            user.userId?.let {
                firestore.collection("users").document(it).set(user).await()
                dataOrException.data = user
            }
        } catch (e: Exception) {
            dataOrException.e = e
        } finally {
            dataOrException.loading = false
        }
        return dataOrException
    }


    /**
     * Uploads an image to Firebase Storage.
     *
     * @param imageUri the URI of the image to upload.
     * @param previousImageUrl the URL of the previous image to delete before uploading the new one.
     * @return the download URL of the uploaded image as a String.
     */

    suspend fun uploadImage(imageUri: Uri, previousImageUrl: String?): String {
        // Eliminar la imagen anterior
        previousImageUrl?.let {
            deleteImage(it)
        }

        val storageRef = storage.reference
        val fileName = UUID.randomUUID().toString() + ".jpg"
        val fileRef = storageRef.child("avatars").child(fileName)

        val uploadTask = fileRef.putFile(imageUri)
        uploadTask.await()

        return fileRef.downloadUrl.await().toString()
    }

    /**
     * Deletes an image from Firebase Storage.
     *
     * @param imageUrl the URL of the image to delete.
     */

    suspend fun deleteImage(imageUrl: String) {
        try {
            val imageRef = storage.getReferenceFromUrl(imageUrl)
            imageRef.delete().await()
        } catch (e: Exception) {
            Log.e(TAG, "Error deleting image", e)
        }
    }

    /**
     * Retrieves the download URL of an image from Firebase Storage.
     *
     * @param imagePath the path of the image in Firebase Storage.
     * @return the download URL of the image as a String.
     */

    suspend fun getImageUrl(imagePath: String): String {
        val storageRef = storage.reference
        val imageRef = storageRef.child(imagePath)

        return try {
            imageRef.downloadUrl.await().toString()
        } catch (e: Exception) {
            Log.e(TAG, "Error getting download URL", e)
            ""
        }
    }

}


