package com.example.foodiefinder.ui.navigation


enum class FoodScreens {
    SplashScreen,
    LoginScreen,
    RegisterScreen,
    FoodHomeScreen,
    FoodFavoriteScreen,
    FoodProfileScreen,
    FoodDetailsScreen,
    FoodAboutScreen;

    companion object{
        fun fromRoute(route:String): FoodScreens
        = when(route?.substringBefore("/")){
            SplashScreen.name -> SplashScreen
            RegisterScreen.name -> LoginScreen
            RegisterScreen.name -> RegisterScreen
            FoodFavoriteScreen.name -> FoodFavoriteScreen
            FoodHomeScreen.name -> FoodHomeScreen
            FoodProfileScreen.name -> FoodProfileScreen
            FoodDetailsScreen.name -> FoodDetailsScreen
            FoodAboutScreen.name -> FoodAboutScreen
            null -> FoodHomeScreen
            else -> throw IllegalArgumentException("Route $route is not recognized")

        }
    }

}