package com.example.foodiefinder.ui.screens.home


import android.annotation.SuppressLint
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.auth.FirebaseAuth
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.example.foodiefinder.R
import com.example.foodiefinder.data.model.RecipeListEvent
import com.example.foodiefinder.ui.components.FoodDivider
import com.example.foodiefinder.ui.components.RecipeCardLittle
import com.example.foodiefinder.ui.components.SearchBarExample
import com.example.foodiefinder.ui.navigation.FoodScreens
import com.example.foodiefinder.ui.screens.auth.AuthViewModel
import com.google.gson.Gson


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun FoodHomeScreen(
    navController: NavController,
    viewModel: HomeScreenViewModel = hiltViewModel(),
    mainViewModel: FoodSearchViewModel = hiltViewModel(),
    authViewModel: AuthViewModel = hiltViewModel()
) {


    Surface(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .padding(20.dp)
        ) {
            Spacer(modifier = Modifier.height(16.dp))
            WelcomeUser(navController, homeScreenViewModel = viewModel)
            HomeContent(navController, mainViewModel = mainViewModel)
        }
    }


    DisposableEffect(Unit) {
        onDispose {
            mainViewModel.onEvent(RecipeListEvent.Search(""))
        }
    }

}


@Composable
fun HomeContent(
    navController: NavController,
    mainViewModel: FoodSearchViewModel = hiltViewModel()
) {
    val searchQueryState = remember { mutableStateOf("") }
    var isSearching by remember { mutableStateOf(false) }


    Column(modifier = Modifier.padding(top = 16.dp)) {

        SearchBarExample(
            query = searchQueryState.value,
            onQueryChange = { newQuery ->
                searchQueryState.value = newQuery
                mainViewModel.onEvent(RecipeListEvent.Search(newQuery))
                isSearching = newQuery.isNotEmpty()
            },
            searchQueryState = searchQueryState
        )


        val state = mainViewModel.state.value
        if (state.isLoading) {
            CircularProgressIndicator(
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        }

        val gson = Gson()

        LazyColumn(modifier = Modifier.padding(top = 5.dp)) {
            items(state.recipe) { recipe ->
                RecipeCardLittle(
                    data = recipe,
                    searchQueryState = searchQueryState.value,
                    onItemClick = {

                        val recipeJson = gson.toJson(recipe.recipe)
                        navController.navigate(
                            "${FoodScreens.FoodDetailsScreen.name}?recipeJson=${
                                Uri.encode(
                                    recipeJson
                                )
                            }"
                        )

                    }
                )
            }
        }

    }

}


@Composable
fun WelcomeUser(
    navController: NavController,
    homeScreenViewModel: HomeScreenViewModel = hiltViewModel()
) {
    val userState by homeScreenViewModel.userState.observeAsState()

    LaunchedEffect(Unit) {
        val currentUserUid = FirebaseAuth.getInstance().currentUser?.uid
        homeScreenViewModel.getUserData(currentUserUid)
    }


    Column(modifier = Modifier.fillMaxWidth()) {

        when {
            userState?.loading == true -> {

                CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
            }

            userState?.e != null -> {
                Text(
                    text = "Error: ${userState!!.e?.message}",
                    style = TextStyle(fontSize = 20.sp, color = Color.Red)
                )
            }

            userState?.data != null -> {
                val name = userState?.data?.get("name") as? String ?: "Name not available"
                val avatarUrl = userState?.data?.get("avatarUrl") as? String

                val text = buildAnnotatedString {
                    withStyle(
                        style = SpanStyle(
                            color = MaterialTheme.colorScheme.primary,
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Light,
                            fontFamily = MaterialTheme.typography.titleLarge.fontFamily
                        )
                    ) {
                        append(stringResource(id = R.string.welcome_append))
                    }
                    withStyle(
                        style = SpanStyle(
                            color = MaterialTheme.colorScheme.primary,
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = MaterialTheme.typography.titleLarge.fontFamily
                        )
                    ) {
                        append(", $name!")
                    }
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = text,
                            style = MaterialTheme.typography.titleLarge,
                            color = MaterialTheme.colorScheme.secondary,
                            modifier = Modifier.align(Alignment.Start)
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            text = stringResource(id = R.string.phrase_cook),
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.outline,
                            modifier = Modifier.align(Alignment.Start)
                        )
                    }

                    avatarUrl?.let { url ->
                        Image(
                            painter =
                            rememberAsyncImagePainter(
                                ImageRequest.Builder
                                    (LocalContext.current).data(data = url)
                                    .apply(block = fun ImageRequest.Builder.() {
                                        crossfade(true)
                                        error(R.drawable.error)
                                    }).build()
                            ),
                            contentDescription = stringResource(id = R.string.person_icon),
                            modifier = Modifier
                                .size(80.dp)
                                .clip(CircleShape)
                                .align(Alignment.CenterVertically),
                            contentScale = ContentScale.Crop,
                        )
                    }

                }

            }

        }

        Spacer(modifier = Modifier.height(16.dp))
        FoodDivider(thickness = 3.dp)
    }


}




