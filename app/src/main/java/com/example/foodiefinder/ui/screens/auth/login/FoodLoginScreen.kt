package com.example.foodiefinder.ui.screens.auth.login


import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.material3.Surface
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavController
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.foodiefinder.R
import com.example.foodiefinder.ui.components.forms.LoginForm
import com.example.foodiefinder.ui.navigation.FoodScreens
import com.example.foodiefinder.ui.screens.auth.AuthViewModel


@Composable
fun FoodLoginScreen(
    navController: NavController,
    authViewModel: AuthViewModel = hiltViewModel(),
    viewModel: LoginScreenViewModel = hiltViewModel()
) {

    val emailError by viewModel.emailError.observeAsState()
    val emailValid by viewModel.emailValid.observeAsState(false)
    val passwordError by viewModel.passwordError.observeAsState()
    val passwordValid by viewModel.passwordValid.observeAsState(false)

    val loading by viewModel.loading.observeAsState(false)

    Surface(
        modifier = Modifier
            .fillMaxSize()
    ) {

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(16.dp)
        ) {

            LoginText()

            Spacer(modifier = Modifier.height(15.dp))

            LoginForm(
                loading = loading,
                emailError = emailError,
                passwordError = passwordError,
                emailValid = emailValid,
                passwordValid = passwordValid,
                onEmailChange = { viewModel.onEmailChange(it) },
                onPasswordChange = { viewModel.onPasswordChange(it) }


            ) { email, password ->

                viewModel.clearErrorMessages()

                viewModel.signInWithEmailAndPassword(email, password, authViewModel) {
                    navController.navigate(FoodScreens.FoodHomeScreen.name) {
                        popUpTo(FoodScreens.LoginScreen.name) { inclusive = true }
                    }
                }

            }

            Spacer(modifier = Modifier.height(15.dp))
            Row(
                modifier = Modifier.padding(15.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {

                Text(
                    text = stringResource(id = R.string.dontAccount),
                    color = MaterialTheme.colorScheme.secondary
                )
                Spacer(modifier = Modifier.width(5.dp))
                Text(
                    text = stringResource(id = R.string.signUp),
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.clickable {
                        navController.navigate(FoodScreens.RegisterScreen.name)
                    }
                )
            }
        }

    }
}


@Composable
fun LoginText() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(id = R.string.welcome),
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.padding(5.dp))
        Text(
            text = stringResource(id = R.string.loginAccount),
            fontSize = 16.sp,
            color = MaterialTheme.colorScheme.secondary
        )
    }
}
