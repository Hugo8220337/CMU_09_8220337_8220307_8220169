package ipp.estg.cmu_09_8220169_8220307_8220337.ui.components.utils

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun LightSquaredButton(
    text: String,
    buttonColors: ButtonColors = ButtonDefaults.outlinedButtonColors(containerColor = Color.White),
    onClick: () -> Unit = {},
    textColor: Color = Color.Black,
    buttonModifier: Modifier = Modifier,
    textModifier: Modifier = Modifier,
    fontWeight: FontWeight = FontWeight.Bold,
    textSize: Int = 22
) {
    Button(
        onClick = onClick,
        shape = RoundedCornerShape(8.dp), // Set the radius for rounded corners
        colors = buttonColors,
        modifier = buttonModifier
    ) {
        Text(
            text = text,
            modifier = textModifier,
            textAlign = TextAlign.Center,
            color = textColor,
            fontSize = textSize.sp,
            fontWeight = fontWeight,
            style = TextStyle(
                shadow = Shadow(
                    color = Color.Black, // Border color
                    offset = Offset(0f, 0f), // No offset, centered shadow
                    blurRadius = 2f // Adjust this to make the border thicker or thinner
                )
            )
        )
    }
}