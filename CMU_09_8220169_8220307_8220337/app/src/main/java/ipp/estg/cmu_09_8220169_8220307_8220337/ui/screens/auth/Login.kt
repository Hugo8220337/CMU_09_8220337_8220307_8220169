package ipp.estg.cmu_09_8220169_8220307_8220337.ui.screens.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import ipp.estg.cmu_09_8220169_8220307_8220337.R
import ipp.estg.cmu_09_8220169_8220307_8220337.data.firebase.AuthStatus
import ipp.estg.cmu_09_8220169_8220307_8220337.ui.components.LoginFields
import ipp.estg.cmu_09_8220169_8220307_8220337.ui.navigation.Screen
import ipp.estg.cmu_09_8220169_8220307_8220337.ui.theme.CMU_09_8220169_8220307_8220337Theme
import ipp.estg.cmu_09_8220169_8220307_8220337.viewModels.AuthenticationViewModel

@Composable
fun LoginScreen(navController: NavController, authViewModel: AuthenticationViewModel) {

    val authStatus by authViewModel.authState.collectAsState()

    var isError by remember { mutableStateOf(false) }

    // Check if the user is logged in
    LaunchedEffect(authStatus) {
        if (authStatus != AuthStatus.LOADING) {
            when (authStatus) {
                AuthStatus.LOGGED -> navController.navigate(Screen.Home.route)
                AuthStatus.INVALID_LOGIN -> isError = true
                else -> isError = false
            }
        }
    }

    Scaffold { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(30.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(25.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.minilogo),
                    contentDescription = stringResource(id = R.string.app_name),
                    modifier = Modifier.size(200.dp)
                )

                var email by remember { mutableStateOf("") }
                var password by remember { mutableStateOf("") }

                LoginFields(
                    email = email,
                    password = password,
                    onEmailChange = { email = it },
                    onPasswordChange = { password = it },
                    onLoginClick = {
                        authViewModel.login(email, password)
                    }
                )

                if (isError) {
                    Text(
                        text = "Invalid login",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = Color.Red
                        )
                    )
                }
            }
        }
    }
}

