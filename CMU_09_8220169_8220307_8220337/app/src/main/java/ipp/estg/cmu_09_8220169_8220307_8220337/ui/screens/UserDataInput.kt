package ipp.estg.cmu_09_8220169_8220307_8220337.ui.screens

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import ipp.estg.cmu_09_8220169_8220307_8220337.data.room.models.User
import ipp.estg.cmu_09_8220169_8220307_8220337.viewModels.UserViewModel
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserDataInput(
    navController: NavController,
    userViewModel: UserViewModel = viewModel()
) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var weight by remember { mutableStateOf("") }
    var height by remember { mutableStateOf("") }
    var birthDate by remember { mutableStateOf("") }

    var showDatePicker by remember { mutableStateOf(false) }
    var selectedDate by remember { mutableStateOf<Long?>(null) }

    val dateFormatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    showDatePicker = false
                    selectedDate?.let {
                        birthDate = dateFormatter.format(Date(it))
                    }
                }) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) {
                    Text("Cancelar")
                }
            }
        ) {
            DatePicker(
                state = rememberDatePickerState(
                    initialSelectedDateMillis = selectedDate ?: System.currentTimeMillis()
                ),
                showModeToggle = false
            )
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Title
        Text(
            text = "Inserir Dados do Utilizador",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        // Name Field
        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Nome") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        // Email Field
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            singleLine = true
        )

        // Weight Field
        OutlinedTextField(
            value = weight,
            onValueChange = { weight = it },
            label = { Text("Peso (kg)") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            singleLine = true
        )

        // Height Field
        OutlinedTextField(
            value = height,
            onValueChange = { height = it },
            label = { Text("Altura (m)") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            singleLine = true
        )

        // Birth Date Field
        OutlinedTextField(
            value = birthDate,
            onValueChange = { },
            label = { Text("Data de Nascimento") },
            modifier = Modifier.fillMaxWidth(),
            readOnly = true,
            trailingIcon = {
                IconButton(onClick = { showDatePicker = true }) {
                    Icon(
                        imageVector = Icons.Default.DateRange,
                        contentDescription = "Selecionar data"
                    )
                }
            }
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Save Button
        Button(
            onClick = {
                // Convertendo os campos para os tipos necessários
                val weightFloat = weight.toDoubleOrNull() ?: 0f
                val heightFloat = height.toDoubleOrNull() ?: 0f

                // Chama a função de registro do usuário
                userViewModel.registUser(
                    user = User(
                        name = name,
                        email = email,
                        birthDate = birthDate,
                        height = heightFloat as Double,
                        weight = weightFloat as Double
                    ),
                    onSuccess = {
                        // Exibe uma mensagem de sucesso ou navega para outra tela
                        Toast.makeText(
                            navController.context,
                            "Usuário registrado com sucesso",
                            Toast.LENGTH_SHORT
                        ).show()
                        navController.popBackStack()
                    },
                    onError = { errorMessage ->
                        // Exibe a mensagem de erro
                        Toast.makeText(
                            navController.context,
                            errorMessage,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
        ) {
            Text("Guardar")
        }

        // Cancel Button
        OutlinedButton(
            onClick = { navController.popBackStack() },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
        ) {
            Text("Cancelar")
        }
    }
}