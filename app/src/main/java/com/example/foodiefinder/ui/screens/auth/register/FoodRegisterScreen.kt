package com.example.foodiefinder.ui.screens.auth.register


import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddAPhoto
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavController
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import com.example.foodiefinder.R
import com.example.foodiefinder.data.model.MUser
import com.example.foodiefinder.ui.components.forms.RegisterForm
import com.example.foodiefinder.ui.navigation.FoodScreens
import com.example.foodiefinder.ui.screens.auth.login.LoginScreenViewModel
import com.example.foodiefinder.ui.screens.auth.AuthViewModel


@Composable
fun FoodRegisterScreen(
    userProfile: MUser,
    navController: NavController,
    authViewModel: AuthViewModel,
    loginViewModel: LoginScreenViewModel = hiltViewModel(),
    registerViewModel: RegisterScreenViewModel = hiltViewModel()
) {

    val nameError by registerViewModel.nameError.observeAsState()
    val emailError by registerViewModel.emailError.observeAsState()
    val passwordError by registerViewModel.passwordError.observeAsState()

    var userProfile by remember { mutableStateOf(userProfile) }
    var avatarUri by remember { mutableStateOf<Uri?>(null) }


    val getContent = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            userProfile = userProfile.copy(avatarUrl = it.toString())
            avatarUri = it
        }
    }

    val nameValid by registerViewModel.nameValid.observeAsState(false)
    val emailValid by registerViewModel.emailValid.observeAsState(false)
    val passwordValid by registerViewModel.passwordValid.observeAsState(false)

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Box(
            modifier = Modifier
                .size(100.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primary)
                .clickable {
                    getContent.launch("image/*")
                },
            contentAlignment = Alignment.Center
        ) {
            if (userProfile.avatarUrl != null) {
                Image(
                    painter = rememberAsyncImagePainter(
                        model = userProfile.avatarUrl
                    ),
                    contentDescription = stringResource(id = R.string.avatar),
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(CircleShape)
                )
            } else {
                Icon(
                    imageVector = Icons.Default.AddAPhoto,
                    contentDescription = stringResource(id = R.string.photo),
                    tint = Color.White,
                    modifier = Modifier.size(64.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        RegisterText()

        RegisterForm(
            loading = false,
            nameError = nameError,
            nameValid = nameValid,
            emailError = emailError,
            passwordError = passwordError,
            emailValid = emailValid,
            passwordValid = passwordValid,
            onNameChange = { registerViewModel.onNameChange(it) },
            onEmailChange = { registerViewModel.onEmailChange(it) },
            onPasswordChange = { registerViewModel.onPasswordChange(it) }
        ) { name, email, password ->
            registerViewModel.createUserWithEmailPassword(name, email, password, avatarUri) {
                authViewModel.authenticate(email, password)
                navController.navigate(FoodScreens.FoodHomeScreen.name) {
                    popUpTo(FoodScreens.RegisterScreen.name) { inclusive = true }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.padding(15.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(id = R.string.loginAccount),
                color = MaterialTheme.colorScheme.secondary
            )
            Spacer(modifier = Modifier.width(5.dp))
            Text(
                text = stringResource(id = R.string.login),
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.clickable {
                    navController.navigate(FoodScreens.LoginScreen.name)
                }
            )
        }
    }

}


@Composable
fun RegisterText() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,

        ) {
        Text(
            text = stringResource(id = R.string.getStarted),
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.padding(5.dp))
        Text(
            text = stringResource(id = R.string.createAccount),
            fontSize = 16.sp,
            color = MaterialTheme.colorScheme.secondary
        )
    }
}

