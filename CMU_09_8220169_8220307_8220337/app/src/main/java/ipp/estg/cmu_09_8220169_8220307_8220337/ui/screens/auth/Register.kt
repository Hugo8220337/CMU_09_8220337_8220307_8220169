package ipp.estg.cmu_09_8220169_8220307_8220337.ui.screens.auth

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import ipp.estg.cmu_09_8220169_8220307_8220337.ui.components.utils.LightSquaredButton
import ipp.estg.cmu_09_8220169_8220307_8220337.ui.navigation.Screen
import ipp.estg.cmu_09_8220169_8220307_8220337.ui.theme.CMU_09_8220169_8220307_8220337Theme

@Composable
fun RegisterScreen(navController: NavController) {
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

            TermsOfService()
        }
    }

}

@Composable
private fun Header() {
    Row(
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 50.dp, horizontal = 40.dp)
    ) {

        Text(
            text = "Register",
            fontSize = 55.sp,
            textAlign = TextAlign.Center
        )

    }

}

@Composable
private fun Form(navController: NavController) {
    var userName by remember {
        mutableStateOf(TextFieldValue())
    }

    var email by remember {
        mutableStateOf(TextFieldValue())
    }

    var password by remember {
        mutableStateOf(TextFieldValue())
    }

    var password2 by remember {
        mutableStateOf(TextFieldValue())
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxWidth()
    ) {

        // Username TextField
        TextField(
            value = userName,
            onValueChange = { userName = it },
            label = { Text("Username") },
            modifier = Modifier.fillMaxWidth(0.85f) // Adjust the width (85% of screen width)
        )

        Spacer(modifier = Modifier.height(30.dp))


        // Email TextField
        TextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth(0.85f) // Adjust the width (85% of screen width)
        )

        Spacer(modifier = Modifier.height(30.dp))

        // Password TextField
        TextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            visualTransformation = PasswordVisualTransformation(), // Hide password text
            modifier = Modifier.fillMaxWidth(0.85f) // Same width as email field
        )

        Spacer(modifier = Modifier.height(30.dp))

        // Repeat Password TextField
        TextField(
            value = password2,
            onValueChange = { password2 = it },
            label = { Text("Repeat Password") },
            visualTransformation = PasswordVisualTransformation(), // Hide password text
            modifier = Modifier.fillMaxWidth(0.85f) // Same width as email field
        )

        Spacer(modifier = Modifier.height(45.dp))

        // Register Button
        LightSquaredButton(
            text = "Register",
            buttonModifier = Modifier
                .fillMaxWidth(0.85f)
                .height(80.dp)
                .padding(vertical = 10.dp),
            textModifier = Modifier.align(Alignment.CenterHorizontally),
            fontWeight = FontWeight.Normal,
            textColor = MaterialTheme.colorScheme.surface,
            textSize = 30,
            buttonColors = ButtonDefaults.outlinedButtonColors(containerColor = MaterialTheme.colorScheme.primary),
            onClick = {
                navController.navigate(Screen.Login.route)
            }
        )

    }
}

@Composable
private fun TermsOfService() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 20.dp, horizontal = 40.dp),
    ) {
        Text(
            text = buildAnnotatedString {
                append("By clicking Register, you agree to 75 Hard's ")
                withStyle(style = SpanStyle(textDecoration = TextDecoration.Underline)) {
                    append("Terms of Service")
                }
                append(" and ")
                withStyle(style = SpanStyle(textDecoration = TextDecoration.Underline)) {
                    append("Privacy Policy")
                }
            },
            fontSize = 15.sp,
            textAlign = TextAlign.Center
        )
    }
}

@Preview(showBackground = true)
@Composable
fun RegisterPreview() {
    CMU_09_8220169_8220307_8220337Theme {
        val navController = rememberNavController() // Create NavController to track current route

        RegisterScreen(navController)
    }
}