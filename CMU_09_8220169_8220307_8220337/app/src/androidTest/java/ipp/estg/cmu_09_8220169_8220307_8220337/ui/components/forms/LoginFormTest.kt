package ipp.estg.cmu_09_8220169_8220307_8220337.ui.components.forms

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import ipp.estg.cmu_09_8220169_8220307_8220337.ui.components.forms.LoginFields as LoginFields1


@RunWith(AndroidJUnit4::class)
class LoginFieldsTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun preferencesForm_rendersCorrectly() {
        composeTestRule.setContent {

            // Set up the composable
            LoginFields1(
                email = "teste@teste.com",
                password = "testeteste",
                onEmailChange = {},
                onPasswordChange = {},
                onLoginClick = {}
            )
        }

        // Assert that all components are present
        composeTestRule.onNodeWithTag("emailField").assertExists()
        composeTestRule.onNodeWithTag("passwordField").assertExists()
        composeTestRule.onNodeWithTag("loginButton").assertExists()
    }

    @Test
    fun testLoginButtonClick() {
        var isLoginClicked = false
        composeTestRule.setContent {
            LoginFields1(
                email = "test@example.com",
                password = "password123",
                onEmailChange = {},
                onPasswordChange = {},
                onLoginClick = { isLoginClicked = true }
            )
        }

        // Click the login button
        composeTestRule.onNodeWithTag("loginButton").performClick()

        // Assert the login click is triggered
        assert(isLoginClicked)
    }

}