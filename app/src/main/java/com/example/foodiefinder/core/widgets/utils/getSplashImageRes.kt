package com.example.foodiefinder.core.widgets.utils

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalConfiguration
import com.example.foodiefinder.R


/**
 * A composable function that returns the appropriate splash screen image resource based on the current theme.
 *
 * @return the resource ID of the splash screen image.
 */

@Composable
fun getSplashImageRes(): Int {
    val configuration = LocalConfiguration.current

    val isDarkTheme =
        when (configuration.uiMode and android.content.res.Configuration.UI_MODE_NIGHT_MASK) {
            android.content.res.Configuration.UI_MODE_NIGHT_YES -> true
            android.content.res.Configuration.UI_MODE_NIGHT_NO -> false
            else -> false
        }

    return if (isDarkTheme) {
        R.drawable.splash_dark
    } else {
        R.drawable.splash_light
    }
}