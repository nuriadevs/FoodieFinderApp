package com.example.foodiefinder.ui.screens.auth.login

/**
 * A data class representing the loading state of an asynchronous operation.
 */
data class LoadingState(val status: Status, val message: String? = null) {

    companion object{
        val SUCCESS = LoadingState(Status.SUCCESS)
        val FAILED = LoadingState(Status.FAILED)
        val LOADING = LoadingState(Status.LOADING)
        val IDLE = LoadingState(Status.IDLE)
    }

    enum class Status {
        SUCCESS,
        FAILED,
        LOADING,
        IDLE

    }

}
