package com.example.foodiefinder.ui.navigation


import androidx.compose.ui.graphics.Color
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import com.example.foodiefinder.ui.theme.primaryLight


class MyAppNavigationActions(private val navController: NavHostController) {
    fun navigateTo(destination: NavItem) {
        navController.navigate(destination.route) {
            popUpTo(navController.graph.findStartDestination().id) {
                saveState = true
            }
            launchSingleTop = true
        }
    }
}


data class NavItem(
    val label: String,
    val icon: ImageVector,
    val route: String,
    val selectedColor: Color

)



val listOfNavItems: List<NavItem> = listOf(
    NavItem(
        label = MyAppRoute.HOME,
        icon = Icons.Default.Home,
        route = FoodScreens.FoodHomeScreen.name,
        selectedColor =  primaryLight
    ),

    NavItem(
        label = MyAppRoute.FAVORITE,
        icon = Icons.Default.Favorite,
        route = FoodScreens.FoodFavoriteScreen.name,
        selectedColor = primaryLight
    ),


    NavItem(
        label = MyAppRoute.PROFILE,
        icon = Icons.Default.AccountCircle,
        route = FoodScreens.FoodProfileScreen.name,
        selectedColor = primaryLight
    ),

    NavItem(
        label = MyAppRoute.ABOUT,
        icon = Icons.Default.Info,
        route = FoodScreens.FoodAboutScreen.name,
        selectedColor =  primaryLight,
    )


)

object MyAppRoute {

    const val HOME = "home"
    const val FAVORITE = "favorite"
    const val PROFILE = "profile"
    const val ABOUT = "about"
}