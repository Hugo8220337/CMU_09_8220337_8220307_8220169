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
class LoginTest{

    @get:Rule
    val composeTestRule = createComposeRule()
    private lateinit var navController: NavController

    @Before
    fun setupLoginScreen() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        navController = NavController(context)
    }

    // Test if the login screen is displayed
    @Test
    fun loginPage_initialState() {
        composeTestRule.setContent {
            LoginScreen(navController)
        }
        // Verify initial UI elements are present
        composeTestRule.onNodeWithTag("logo").assertExists()
    }

    @Test
    fun testLoginScreenComponents() {
        composeTestRule.setContent {
            LoginScreen(navController = navController, authViewModel = viewModel())
        }

        // Assert logo is displayed
        composeTestRule.onNodeWithTag("logo").assertExists()

        // Assert email field is displayed
        composeTestRule.onNodeWithTag("emailField").assertExists()

        // Assert password field is displayed
        composeTestRule.onNodeWithTag("passwordField").assertExists()

        // Assert login button is displayed
        composeTestRule.onNodeWithTag("loginButton").assertExists()
    }

}