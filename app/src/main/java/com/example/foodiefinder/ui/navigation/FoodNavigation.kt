package com.example.foodiefinder.ui.navigation


import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.foodiefinder.data.model.MUser
import com.example.foodiefinder.data.model.RecipeX
import com.example.foodiefinder.ui.screens.about.FoodAboutScreen
import com.example.foodiefinder.ui.screens.auth.AuthViewModel
import com.example.foodiefinder.ui.screens.auth.login.FoodLoginScreen
import com.example.foodiefinder.ui.screens.auth.register.FoodRegisterScreen
import com.example.foodiefinder.ui.screens.details.FoodDetailsScreen
import com.example.foodiefinder.ui.screens.favorite.FoodFavoriteScreen
import com.example.foodiefinder.ui.screens.home.FoodHomeScreen
import com.example.foodiefinder.ui.screens.home.HomeScreenViewModel
import com.example.foodiefinder.ui.screens.profile.FoodProfileScreen
import com.example.foodiefinder.ui.screens.profile.ProfileViewModel
import com.example.foodiefinder.ui.screens.splash.FoodSplashScreen
import com.google.gson.Gson


@Composable
fun FoodNavigation(authViewModel: AuthViewModel) {
    val navController = rememberNavController()
    val isAuthenticated by authViewModel.isAuthenticated.observeAsState(false)

    Scaffold(
        bottomBar = {
            if (isAuthenticated) {
                MyAppBottomNavigation(
                    navController = navController,
                    listOfNavItems = listOfNavItems
                )
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = FoodScreens.SplashScreen.name,

            modifier = Modifier.padding(innerPadding)
        ) {
            composable(FoodScreens.SplashScreen.name) {
                FoodSplashScreen(navController = navController)
            }

            composable(FoodScreens.LoginScreen.name) {
                FoodLoginScreen(navController = navController, authViewModel = authViewModel)
            }

            composable(FoodScreens.RegisterScreen.name) {
                FoodRegisterScreen(navController = navController, authViewModel = authViewModel, userProfile = MUser())
            }

            composable(FoodScreens.FoodHomeScreen.name) {
                val homeViewModel = hiltViewModel<HomeScreenViewModel>()
                FoodHomeScreen(navController = navController, viewModel = homeViewModel)
            }

            composable(
                route = "${FoodScreens.FoodDetailsScreen.name}?recipeJson={recipeJson}",
                arguments = listOf(navArgument("recipeJson") { type = NavType.StringType })
            ) { backStackEntry ->
                val recipeJson = backStackEntry.arguments?.getString("recipeJson")
                val recipe = Gson().fromJson(recipeJson, RecipeX::class.java)
                FoodDetailsScreen(recipe = recipe, navController = navController)
            }

            composable(FoodScreens.FoodFavoriteScreen.name) {
                val homeViewModel = hiltViewModel<HomeScreenViewModel>()
                FoodFavoriteScreen(navController = navController, homeScreenViewModel = homeViewModel)
            }

            composable(FoodScreens.FoodProfileScreen.name) {
                val homeViewModel = hiltViewModel<ProfileViewModel>()
                FoodProfileScreen(navController = navController, viewModel = homeViewModel)
            }

            composable(FoodScreens.FoodAboutScreen.name) {
                FoodAboutScreen(navController = navController, authViewModel = authViewModel)
            }

        }
    }
}

