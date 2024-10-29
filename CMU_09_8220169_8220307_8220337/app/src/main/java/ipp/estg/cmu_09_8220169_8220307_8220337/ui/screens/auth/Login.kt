package ipp.estg.cmu_09_8220169_8220307_8220337.ui.screens.auth

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import ipp.estg.cmu_09_8220169_8220307_8220337.R
import ipp.estg.cmu_09_8220169_8220307_8220337.ui.components.utils.LightSquaredButton
import ipp.estg.cmu_09_8220169_8220307_8220337.ui.navigation.Screen
import ipp.estg.cmu_09_8220169_8220307_8220337.ui.theme.CMU_09_8220169_8220307_8220337Theme

@SuppressLint("SuspiciousIndentation")
@Composable
fun LoginScreen(navController: NavController) {
    val scrollState = rememberScrollState()

    Scaffold { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(scrollState),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Header()

            Form(navController)
        }
    }
}

@Composable
private fun Header() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .padding(top = 35.dp)
    ) {
        Image(
            painter = painterResource(id = R.drawable.minilogo),
            contentDescription = "Leaflings Logo",
            modifier = Modifier
                .size(200.dp)
        )
    }
}

@Composable
private fun Form(navController: NavController) {
    var email by remember {
        mutableStateOf(TextFieldValue())
    }

    var password by remember {
        mutableStateOf(TextFieldValue())
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .padding(vertical = 60.dp)
            .fillMaxWidth()
    ) {
        // Email TextField
        TextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth(0.85f) // Adjust the width (85% of screen width)
        )

        Spacer(modifier = Modifier.height(50.dp))

        // Password TextField
        TextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            visualTransformation = PasswordVisualTransformation(), // Hide password text
            modifier = Modifier.fillMaxWidth(0.85f) // Same width as email field
        )

        Spacer(modifier = Modifier.height(30.dp))

        // Login Button
        LightSquaredButton(
            text = "Login",
            buttonModifier = Modifier
                .fillMaxWidth(0.85f) // Make the button the same width as the TextFields
                .height(80.dp) // Adjust height if needed
                .padding(vertical = 10.dp),
            textModifier = Modifier.align(Alignment.CenterHorizontally),
            fontWeight = FontWeight.Normal,
            textSize = 30,
            buttonColors = ButtonDefaults.outlinedButtonColors(containerColor = MaterialTheme.colorScheme.primary),
            onClick = {
                navController.navigate(Screen.Home.route)
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun LoginPreview() {
    CMU_09_8220169_8220307_8220337Theme {
        val navController = rememberNavController()

        LoginScreen(navController)
    }
}