package ipp.estg.cmu_09_8220169_8220307_8220337.ui.components.forms

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import ipp.estg.cmu_09_8220169_8220307_8220337.R
import ipp.estg.cmu_09_8220169_8220307_8220337.ui.components.utils.MyDatePickerDialog
import ipp.estg.cmu_09_8220169_8220307_8220337.ui.components.utils.ReadonlyTextField

@Composable
fun RegisterFields(
    username: String,
    email: String,
    password: String,
    birthDate: String,
    weight: Double,
    height: Double,
    onUsernameChange: (String) -> Unit,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onBirthDateChange: (String) -> Unit,
    onWeightChange: (Double) -> Unit,
    onHeightChange: (Double) -> Unit,
    onRegisterClick: () -> Unit
) {
    var showDatePicker by remember {
        mutableStateOf(false)
    }

    var weightText by remember { mutableStateOf(weight.toString()) }
    var heightText by remember { mutableStateOf(height.toString()) }


    if (showDatePicker) {
        MyDatePickerDialog(
            onDateSelected = { onBirthDateChange(it) },
            onDismiss = { showDatePicker = false }
        )
    }

    TextField(
        value = username,
        onValueChange = { onUsernameChange(it) },
        label = { Text(stringResource(id = R.string.username)) },
        modifier = Modifier.fillMaxWidth(0.85f)
            .testTag("usernameField"),
    )

    TextField(
        value = email,
        onValueChange = { onEmailChange(it) },
        label = { Text(stringResource(id = R.string.email)) },
        modifier = Modifier.fillMaxWidth(0.85f)
            .testTag("emailField"),
    )

    TextField(
        value = password,
        onValueChange = { onPasswordChange(it) },
        label = { Text(stringResource(id = R.string.password)) },
        visualTransformation = PasswordVisualTransformation(),
        modifier = Modifier.fillMaxWidth(0.85f)
            .testTag("passwordField"),
    )

    ReadonlyTextField(
        value = TextFieldValue(birthDate),
        onValueChange = { onBirthDateChange(it.text) },
        onClick = {
            showDatePicker = true
        },
        label = {
            Text(text = "Date")
        },
        modifier = Modifier
            .fillMaxWidth(0.85f)
            .testTag("birthDateField"),
    )


    TextField(
        value = weightText,
        onValueChange = { value ->
            weightText = value
            value.toDoubleOrNull()?.let {
                onWeightChange(it)
            }
        },
        label = { Text(stringResource(id = R.string.weight)) },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        modifier = Modifier.fillMaxWidth(0.85f)
            .testTag("weightField"),
    )


    TextField(
        value = heightText,
        onValueChange = { value ->
            heightText = value
            value.toDoubleOrNull()?.let {
                onHeightChange(it)
            }
        },
        label = { Text(stringResource(id = R.string.height)) },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        modifier = Modifier.fillMaxWidth(0.85f)
            .testTag("heightField"),
    )

    Button(
        onClick = {
            onRegisterClick()
        },
        modifier = Modifier
            .fillMaxWidth(0.85f)
            .padding(vertical = 8.dp)
            .testTag("registerButton"),
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