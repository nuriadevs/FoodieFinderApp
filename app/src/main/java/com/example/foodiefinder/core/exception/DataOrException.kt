package com.example.foodiefinder.core.exception


/**
 * A data class that holds a result, its loading state, and an optional exception.
 *
 * @param T the type of data.
 * @param Boolean the loading state.
 * @param E the type of exception.
 * @property data the result data, null if not available.
 * @property loading indicates if the operation is in progress.
 * @property e an optional exception if an error occurred.
 */

data class DataOrException<T, Boolean, E : Exception?>(
    var data: T? = null,
    var loading: Boolean? = null,
    var e: E? = null)