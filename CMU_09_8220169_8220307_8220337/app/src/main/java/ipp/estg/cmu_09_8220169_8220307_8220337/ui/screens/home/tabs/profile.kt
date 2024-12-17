package ipp.estg.cmu_09_8220169_8220307_8220337.ui.screens.home.tabs

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import ipp.estg.cmu_09_8220169_8220307_8220337.R
import ipp.estg.cmu_09_8220169_8220307_8220337.data.room.models.User
import ipp.estg.cmu_09_8220169_8220307_8220337.ui.components.utils.ErrorScreen
import ipp.estg.cmu_09_8220169_8220307_8220337.ui.navigation.Screen
import ipp.estg.cmu_09_8220169_8220307_8220337.viewModels.AuthViewModel
import ipp.estg.cmu_09_8220169_8220307_8220337.viewModels.UserViewModel
import ipp.estg.mobile.ui.components.utils.Loading

@Composable
fun ProfileScreen(
    navController: NavController,
    userViewModel: UserViewModel = viewModel(),
    authViewModel: AuthViewModel = viewModel()
) {

    val user by userViewModel.user.collectAsState()
    val isLoading by userViewModel.isLoading.collectAsState()
    val error by userViewModel.errorMessage.collectAsState()

    LaunchedEffect(Unit) {
        userViewModel.getUser()
    }

    if (isLoading) {
        Loading()
    } else if (error != null) {
        ErrorScreen(error)
    } else if (user != null) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Profile Card with image and name
            ProfileCard(user!!)

            Spacer(modifier = Modifier.height(24.dp))

            // User Information Card
            UserInfoCard(user!!)

            Spacer(modifier = Modifier.height(32.dp))

            Divider(modifier = Modifier.fillMaxWidth())

            Spacer(modifier = Modifier.height(35.dp))

            // Edit Profile Button
            EditProfileButton(navController)

            Spacer(modifier = Modifier.height(20.dp))

            // Logout Button
            LogoutButton(navController, authViewModel)
        }
    }
}

@Composable
fun ProfileCard(user: User) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White,
            contentColor = Color.Black
        ),
        elevation = CardDefaults.cardElevation(8.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(16.dp)
        ) {
            ProfileImage(image = painterResource(id = R.drawable.minilogo))

            Spacer(modifier = Modifier.width(32.dp))

            Text(
                text = user.name,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
        }
    }
}

@Composable
fun UserInfoCard(user: User) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White,
            contentColor = Color.Black
        ),
        elevation = CardDefaults.cardElevation(8.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .height(250.dp) // Adjusted height to fit content better
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text(text = stringResource(id = R.string.email) + ": " + user.email, fontSize = 16.sp, color = Color.Gray)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = stringResource(id = R.string.birth_date) + ": " + user.birthDate, fontSize = 16.sp, color = Color.Gray)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = stringResource(id = R.string.height) + ": " + user.height, fontSize = 16.sp, color = Color.Gray)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = stringResource(id = R.string.weight) + ": " + user.weight, fontSize = 16.sp, color = Color.Gray)
        }
    }
}

@Composable
fun ProfileImage(image: Painter) {
    Image(
        painter = image,
        contentDescription = stringResource(id = R.string.profile_image),
        contentScale = ContentScale.Crop, // Garante que a imagem seja cortada corretamente
        modifier = Modifier
            .size(120.dp) // Define o tamanho do círculo
            .clip(CircleShape) // Corta a imagem em forma de círculo
    )
}

@Composable
fun EditProfileButton(navController: NavController) {
    Button(
        onClick = {
            navController.navigate(Screen.EditProfile.route)
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 32.dp)
            .height(50.dp)
    ) {
        Text(text = stringResource(id = R.string.edit_profile))
    }
}

@Composable
fun LogoutButton(navController: NavController, authViewModel: AuthViewModel) {
    Button(
        onClick = {
            navController.navigate(Screen.Start.route)
            authViewModel.logout()
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 32.dp)
            .height(50.dp)
    ) {
        Text(text = stringResource(id = R.string.logout))
    }
}




