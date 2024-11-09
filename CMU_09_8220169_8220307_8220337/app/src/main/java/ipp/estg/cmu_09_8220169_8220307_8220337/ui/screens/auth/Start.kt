package ipp.estg.cmu_09_8220169_8220307_8220337.ui.screens.auth


import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import ipp.estg.cmu_09_8220169_8220307_8220337.R
import ipp.estg.cmu_09_8220169_8220307_8220337.ui.components.utils.LightSquaredButton
import ipp.estg.cmu_09_8220169_8220307_8220337.ui.navigation.Screen
import ipp.estg.cmu_09_8220169_8220307_8220337.ui.theme.CMU_09_8220169_8220307_8220337Theme

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun StartScreen(navController: NavController) {
    Scaffold { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(50.dp),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {

            Image(
                painter = painterResource(id = R.drawable.minilogo),
                contentDescription = stringResource(id = R.string.app_name),
                modifier = Modifier
                    .size(350.dp)
            )

            PageBottom(navController)
        }
    }
}


@Composable
private fun PageBottom(navController: NavController) {
    val buttonModifier = Modifier
        .padding(vertical = 11.dp, horizontal = 20.dp)
        .shadow(5.dp)
    val textModifier = Modifier.padding(horizontal = 19.dp, vertical = 5.dp)

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
    ) {


        LightSquaredButton(
            stringResource(id = R.string.login),
            textColor = Color.Black,
            buttonModifier = buttonModifier,
            textModifier = textModifier,
            onClick = {
                navController.navigate(Screen.Login.route)
            }
        )
        LightSquaredButton(
            stringResource(id = R.string.register),
            buttonColors = ButtonDefaults.outlinedButtonColors(containerColor = Color.Black),
            textColor = Color.White,
            buttonModifier = buttonModifier,
            textModifier = textModifier,
            onClick = {
                navController.navigate(Screen.Register.route)
            }
        )

    }
}


@Preview(showBackground = true)
@Composable
fun StartPagePreview() {
    CMU_09_8220169_8220307_8220337Theme {
        val navController = rememberNavController()

        StartScreen(navController)
    }
}