package ipp.estg.cmu_09_8220169_8220307_8220337.ui.screens.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import ipp.estg.cmu_09_8220169_8220307_8220337.R
import ipp.estg.cmu_09_8220169_8220307_8220337.data.firebase.auth.AuthStatus
import ipp.estg.cmu_09_8220169_8220307_8220337.ui.components.forms.RegisterFields
import ipp.estg.cmu_09_8220169_8220307_8220337.ui.navigation.Screen
import ipp.estg.cmu_09_8220169_8220307_8220337.viewModels.AuthViewModel
import ipp.estg.mobile.ui.components.utils.Loading

@Composable
fun RegisterScreen(
    navController: NavController,
    authViewModel: AuthViewModel = viewModel()
) {

    val authStatus by authViewModel.authState.collectAsState()

    var isError by remember { mutableStateOf(false) }

    var username by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var birthDate by remember { mutableStateOf("") }
    var weight by remember { mutableDoubleStateOf(0.0) }
    var height by remember { mutableDoubleStateOf(0.0) }

    LaunchedEffect(authStatus) {
        if(authStatus != AuthStatus.LOADING){
            when(authStatus){
                AuthStatus.LOGGED -> {
                    navController.navigate(Screen.Login.route)
                }
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
                        .testTag("logo")
                )

                Text(
                    text = stringResource(id = R.string.register),
                    color = MaterialTheme.colorScheme.onBackground,
                    style = MaterialTheme.typography.headlineLarge.copy(
                        fontWeight = FontWeight.Bold
                    )
                )

                RegisterFields(
                    username = username,
                    email = email,
                    password = password,
                    birthDate = birthDate,
                    weight = weight,
                    height = height,
                    onUsernameChange = { username = it },
                    onEmailChange = { email = it },
                    onPasswordChange = { password = it },
                    onBirthDateChange = { birthDate = it },
                    onWeightChange = { weight = it },
                    onHeightChange = { height = it },
                    onRegisterClick = {
                        authViewModel.register(email, password, username, birthDate, weight, height)
                    }
                )


                Text(
                    text = buildAnnotatedString {
                        append(stringResource(id = R.string.by_clicking_register_you_agree_to_our))
                        withStyle(style = SpanStyle(textDecoration = TextDecoration.Underline)) {
                            append(stringResource(id = R.string.terms_of_service))
                        }
                        append(stringResource(id = R.string.and))
                        withStyle(style = SpanStyle(textDecoration = TextDecoration.Underline)) {
                            append(stringResource(id = R.string.privacy_policy))
                        }
                    },
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onBackground,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )

                if(authStatus == AuthStatus.LOADING){
                    Loading()
                }

                if (isError) {
                    Text(
                        text = "Error registering",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = Color.Red
                        )
                    )
                }

            }
        }
    }



}

