package ipp.estg.cmu_09_8220169_8220307_8220337.ui.screens.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import ipp.estg.cmu_09_8220169_8220307_8220337.R
import ipp.estg.cmu_09_8220169_8220307_8220337.data.room.models.User
import ipp.estg.cmu_09_8220169_8220307_8220337.ui.components.utils.ErrorScreen
import ipp.estg.cmu_09_8220169_8220307_8220337.ui.components.utils.LoadingScreen
import ipp.estg.cmu_09_8220169_8220307_8220337.viewModels.UserViewModel

@Composable
fun EditProfileScreen(
    navController: NavController,
    userViewModel: UserViewModel = viewModel()
) {
    val user by userViewModel.user.collectAsState()
    val isLoading by userViewModel.isLoading.collectAsState()
    val error by userViewModel.errorMessage.collectAsState()

    // Initialize state with user data or empty strings/zeros
    var name by remember(user) { mutableStateOf(user?.name ?: "") }
    var email by remember(user) { mutableStateOf(user?.email ?: "") }
    var birthDate by remember(user) { mutableStateOf(user?.birthDate ?: "") }
    var weight by remember(user) { mutableDoubleStateOf(user?.weight?.toDouble() ?: 0.0) }
    var height by remember(user) { mutableDoubleStateOf(user?.height?.toDouble() ?: 0.0) }

    // Use LaunchedEffect to fetch user data when the screen is first displayed
    LaunchedEffect(Unit) {
        userViewModel.getUser()
    }

    // Update form fields when user data changes
    LaunchedEffect(user) {
        user?.let {
            name = it.name
            email = it.email
            birthDate = it.birthDate
            weight = it.weight.toDouble()
            height = it.height.toDouble()
        }
    }

    if (isLoading) {
        LoadingScreen()
    } else if (error != null) {
        ErrorScreen(error)
    } else {
        Scaffold { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(20.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .verticalScroll(rememberScrollState()),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(20.dp)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.minilogo),
                        contentDescription = stringResource(id = R.string.app_name),
                        modifier = Modifier.size(200.dp)
                    )

                    Text(
                        text = "Edit Profile",
                        style = MaterialTheme.typography.headlineLarge.copy(
                            fontWeight = FontWeight.Bold
                        )
                    )

                    // Username field
                    OutlinedTextField(
                        value = name,
                        onValueChange = { name = it },
                        label = { Text("Username") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    // Email field
                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it },
                        label = { Text("Email") },
                        modifier = Modifier.fillMaxWidth(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
                    )

                    // Birth date field
                    OutlinedTextField(
                        value = birthDate,
                        onValueChange = { birthDate = it },
                        label = { Text("Birth Date") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    // Weight field
                    OutlinedTextField(
                        value = if (weight == 0.0) "" else weight.toString(),
                        onValueChange = {
                            weight = it.toDoubleOrNull() ?: 0.0
                        },
                        label = { Text("Weight") },
                        modifier = Modifier.fillMaxWidth(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
                    )

                    // Height field
                    OutlinedTextField(
                        value = if (height == 0.0) "" else height.toString(),
                        onValueChange = {
                            height = it.toDoubleOrNull() ?: 0.0
                        },
                        label = { Text("Height") },
                        modifier = Modifier.fillMaxWidth(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
                    )

                    // Save Button
                    Button(
                        onClick = {
                            // Update user information
                            user?.let {
                                val updatedUser = User(
                                    id = it.id,
                                    name = name,
                                    email = email,
                                    birthDate = birthDate,
                                    weight = weight,
                                    height = height
                                )
                                userViewModel.updateUser(updatedUser)
                                navController.navigateUp()
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                    ) {
                        Text("Save Changes")
                    }

                    // Cancel Button
                    OutlinedButton(
                        onClick = { navController.navigateUp() },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                    ) {
                        Text("Cancel")
                    }

                    // Add some space at the bottom
                    Spacer(modifier = Modifier.height(20.dp))
                }
            }
        }
    }
}