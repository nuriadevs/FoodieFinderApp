package com.example.foodiefinder.core.widgets.utils


/**
 * A sealed class representing different states of a resource (data, message).
 *
 * @param T the type of data being managed.
 * @property data the data associated with the resource.
 * @property message an optional message associated with the resource.
 */

sealed class Resource<T>(val data: T? = null, val message: String? = null) {
    class Success<T>(data: T) : Resource<T>(data = data)
    class Error<T>(message: String, data: T? = null) : Resource<T>(data = data, message = message)
    class Loading<T>(data: T? = null) : Resource<T>(data = data)
}