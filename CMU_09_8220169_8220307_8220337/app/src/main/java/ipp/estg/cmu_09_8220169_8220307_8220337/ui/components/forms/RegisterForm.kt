package ipp.estg.cmu_09_8220169_8220307_8220337.ui.components.forms

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import ipp.estg.cmu_09_8220169_8220307_8220337.R

@Composable
fun RegisterFields(
    username: String,
    email: String,
    password: String,
    onUsernameChange: (String) -> Unit,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onRegisterClick: () -> Unit
) {
    TextField(
        value = username,
        onValueChange = { onUsernameChange(it) },
        label = { Text(stringResource(id = R.string.username)) },
        modifier = Modifier.fillMaxWidth(0.85f),
    )

    TextField(
        value = email,
        onValueChange = { onEmailChange(it) },
        label = { Text(stringResource(id = R.string.email)) },
        modifier = Modifier.fillMaxWidth(0.85f),
    )

    TextField(
        value = password,
        onValueChange = { onPasswordChange(it) },
        label = { Text(stringResource(id = R.string.password)) },
        visualTransformation = PasswordVisualTransformation(),
        modifier = Modifier.fillMaxWidth(0.85f),
    )

    Button(
        onClick = {
            onRegisterClick()
        },
        modifier = Modifier
            .fillMaxWidth(0.85f)
            .padding(vertical = 8.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = Color.White
        )
    ) {
        Text(
            stringResource(id = R.string.register),
            style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.Bold
            )
        )
    }
}