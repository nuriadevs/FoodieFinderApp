package com.example.foodiefinder.ui.screens.profile

import android.net.Uri
import android.widget.Toast
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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.example.foodiefinder.R
import com.example.foodiefinder.data.model.MUser
import com.example.foodiefinder.ui.components.SubmitButton


@Composable
fun FoodProfileScreen(
    navController: NavController,
    viewModel: ProfileViewModel = hiltViewModel()
) {
    val user by viewModel.user.collectAsState()
    val updateResult by viewModel.updateResult.collectAsState()

    Scaffold( content = { paddingValues ->
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            user?.let {
                Surface(modifier = Modifier.padding(paddingValues)) {

                    UserProfileForm(user = it, onSave = { updatedUser, imageUri ->
                        viewModel.updateUserWithImage(updatedUser, imageUri)
                    })

                }

                when {
                    updateResult.loading == true -> {
                        CircularProgressIndicator()
                    }

                    updateResult.e != null -> {
                        Text(text = "Error: ${updateResult.e?.message}")
                    }

                    updateResult.data != null -> {
                        Toast.makeText(
                            LocalContext.current,
                            stringResource(id = R.string.update_success),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }


            }
        }
    })
}


@Composable
fun UserProfileForm(user: MUser, onSave: (MUser, Uri?) -> Unit) {
    var name by remember { mutableStateOf(user.name ?: "") }
    var displayName by remember { mutableStateOf(user.displayName ?: "") }
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }


    val nameError = remember { mutableStateOf(false) }
    val displayNameError = remember { mutableStateOf(false) }
    val keyboardController = LocalSoftwareKeyboardController.current
    var loading by remember { mutableStateOf(false) }


    val launcher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let {
                selectedImageUri = it
            }
        }

        Column(
            modifier = Modifier
                .padding(16.dp)

        ) {

            ProfileText()
            Spacer(modifier = Modifier.height(16.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Box(
                    modifier = Modifier
                        .size(120.dp)
                        .clip(CircleShape)
                        .align(Alignment.CenterHorizontally)
                        .clickable {
                            launcher.launch("image/*")
                        }
                ) {


                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {

                        val painter = rememberAsyncImagePainter(
                            ImageRequest.Builder(LocalContext.current)
                                .data(data = selectedImageUri ?: user.avatarUrl ?: "").apply {
                                    placeholder(R.drawable.placeholder)
                                    error(R.drawable.error)
                                }.build()
                        )
                        Image(
                            painter = painter,
                            contentDescription = stringResource(id = R.string.person_icon),
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.size(120.dp),
                        )
}
                        Box( modifier = Modifier.align(Alignment.BottomCenter)) {
                            Icon(
                                imageVector = Icons.Default.Edit,
                                contentDescription = "Edit Icon",
                                tint = Color.White,
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(CircleShape)
                                    .background(Color.Black.copy(alpha = 0.5f))
                                    .padding(8.dp)
                            )

                        }

                }

            }

            Spacer(modifier = Modifier.height(16.dp))

            TextField(
                maxLines = 1,
                value = name,
                onValueChange = {
                    name = it
                    nameError.value = it.isBlank()
                },
                label = { Text(stringResource(id = R.string.name)) },
                isError = nameError.value,
                supportingText = {
                    if (nameError.value) Text(
                        "Name cannot be empty", color = Color.Red,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(start = 16.dp)
                    )
                },
                modifier = Modifier.fillMaxWidth()
            )


            Spacer(modifier = Modifier.height(16.dp))


            TextField(
                value = displayName,
                onValueChange = {
                    displayName = it
                    displayNameError.value = it.isBlank()
                },
                label = { Text(stringResource(id = R.string.display_name)) },
                isError = displayNameError.value,
                supportingText = {
                    if (displayNameError.value)
                        Text(
                        stringResource(id = R.string.no_name), color = Color.Red,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(start = 16.dp)
                    )

                },
                modifier = Modifier.fillMaxWidth()
            )


            Spacer(modifier = Modifier.height(16.dp))

            SubmitButton(
                text = stringResource(id = R.string.save),
                loading = loading,
                validInputs = name.isNotEmpty() && displayName.isNotEmpty(),
                onClick = {
                    val updatedUser = MUser(
                        userId = user.userId,
                        name = name,
                        displayName = displayName,
                        avatarUrl = user.avatarUrl
                    )
                    onSave(updatedUser, selectedImageUri)
                    keyboardController?.hide()
                }
            )

        }

}




@Composable
fun ProfileText() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(id = R.string.update_progile),
                style = MaterialTheme.typography.headlineMedium.copy(fontSize = 28.sp),
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.padding(5.dp))
        }
    }
}

