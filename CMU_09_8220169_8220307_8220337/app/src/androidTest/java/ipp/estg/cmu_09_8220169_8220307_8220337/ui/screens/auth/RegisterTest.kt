package ipp.estg.cmu_09_8220169_8220307_8220337.ui.screens.auth

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class RegisterTest{

    @get:Rule
    val composeTestRule = createComposeRule()
    private lateinit var navController: NavController

    @Before
    fun setupRegisterScreen() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        navController = NavController(context)
    }

    // Test if the login screen is displayed
    @Test
    fun registerPage_initialState() {
        composeTestRule.setContent {
            RegisterScreen(navController)
        }
        // Verify initial UI elements are present
        composeTestRule.onNodeWithTag("logo").assertExists()
    }

    @Test
    fun testRegisterScreenComponents() {
        composeTestRule.setContent {
            RegisterScreen(navController = navController, authViewModel = viewModel())
        }

        // Assert logo is displayed
        composeTestRule.onNodeWithTag("logo").assertExists()

        // Assert username field is displayed
        composeTestRule.onNodeWithTag("usernameField").assertExists()

        // Assert email field is displayed
        composeTestRule.onNodeWithTag("emailField").assertExists()

        // Assert password field is displayed
        composeTestRule.onNodeWithTag("passwordField").assertExists()

        // Assert birth date field is displayed
        composeTestRule.onNodeWithTag("birthDateField").assertExists()

        // Assert weight field is displayed
        composeTestRule.onNodeWithTag("weightField").assertExists()

        // Assert height field is displayed
        composeTestRule.onNodeWithTag("heightField").assertExists()

        // Assert register button is displayed
        composeTestRule.onNodeWithTag("registerButton").assertExists()

    }



}