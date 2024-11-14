package ipp.estg.cmu_09_8220169_8220307_8220337.utils

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.core.content.ContextCompat
import java.io.File
import java.io.FileOutputStream
import java.time.LocalDate


/**
 * Function to check if camera permission is granted.
 */
@Composable
fun checkCameraPermission(context: Context): Boolean {
    return remember {
        ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED
    }
}

/**
 * Function to create a camera permission request launcher.
 */
@Composable
fun requestCameraPermission(onPermissionResult: (Boolean) -> Unit) =
    rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { granted ->
            onPermissionResult(granted)
        }
    )

/**
 * Function to create a camera launcher to capture a photo preview.
 */
@Composable
fun launchCamera(onCaptureResult: (Bitmap?) -> Unit) =
    rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicturePreview(),
        onResult = { bitmap ->
            onCaptureResult(bitmap)
        }
    )

/**
 * Function to save a bitmap image to a file.
 * Returns the file absolute path.
 */
fun saveImageToFile(context: Context, bitmap: Bitmap): String {
    val currentDate = LocalDate.now().toString()
    val filename = "captured_image_${currentDate}.png"
    val file = File(context.filesDir, filename)

    FileOutputStream(file).use { out ->
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
    }

    return file.absolutePath
}

/**
 * Function to get a bitmap image from a file.
 * Returns the bitmap or null if the file does not exist.
 */
fun getImageFromFile(absolutePath: String): Bitmap? {
    val file = File(absolutePath)
    return if (file.exists()) {
        BitmapFactory.decodeFile(file.absolutePath)
    } else {
        null
    }
}

fun getImageFromFileWithDate(context: Context, date: String): Bitmap? {
    val file = File(context.filesDir, "captured_image_${date}.png")
    return if (file.exists()) {
        BitmapFactory.decodeFile(file.absolutePath)
    } else {
        null
    }
}