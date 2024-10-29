package ipp.estg.cmu_09_8220169_8220307_8220337.utils

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.core.content.ContextCompat


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