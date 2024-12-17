package ipp.estg.cmu_09_8220169_8220307_8220337.ui.components.forms

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import ipp.estg.cmu_09_8220169_8220307_8220337.ui.components.forms.RegisterFields as RegisterFields1

@RunWith(AndroidJUnit4::class)
class RegisterFormTest{

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun preferencesForm_rendersCorrectly() {
        composeTestRule.setContent {

            // Set up the composable
            RegisterFields1(
                username = "teste",
                email = "teste@teste.com",
                password = "testeteste",
                birthDate = "01/01/2000",
                weight = 70.0,
                height = 1.70,
                onUsernameChange = {},
                onEmailChange = {},
                onPasswordChange = {},
                onBirthDateChange = {},
                onWeightChange = {},
                onHeightChange = {},
                onRegisterClick = {}
            )
        }

            // Assert that all components are present
            composeTestRule.onNodeWithTag("usernameField").assertExists()
            composeTestRule.onNodeWithTag("emailField").assertExists()
            composeTestRule.onNodeWithTag("passwordField").assertExists()
            composeTestRule.onNodeWithTag("birthDateField").assertExists()
            composeTestRule.onNodeWithTag("weightField").assertExists()
            composeTestRule.onNodeWithTag("heightField").assertExists()
            composeTestRule.onNodeWithTag("registerButton").assertExists()

    }

    @Test
    fun testRegisterButtonClick() {
        var isRegisterClicked = false

        // Configura o conteúdo da composição
        composeTestRule.setContent {
            RegisterFields1(
                username = "teste",
                email = "teste@teste.com",
                password = "testeteste",
                birthDate = "01/01/2000",
                weight = 70.0,
                height = 1.70,
                onUsernameChange = {},
                onEmailChange = {},
                onPasswordChange = {},
                onBirthDateChange = {},
                onWeightChange = {},
                onHeightChange = {},
                onRegisterClick = { isRegisterClicked = true }
            )
        }

        // Executa a ação no componente e verifica o estado
        composeTestRule.onNodeWithTag("registerButton").performClick()

        // Verifica se a ação foi disparada
        assert(isRegisterClicked)
    }

}