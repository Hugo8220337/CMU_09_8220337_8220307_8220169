package ipp.estg.cmu_09_8220169_8220307_8220337.utils

import android.Manifest
import android.content.ContentValues
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Environment
import android.provider.MediaStore
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.core.content.ContextCompat
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream
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
        val buffer = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, buffer)
        out.write(buffer.toByteArray())
    }

    return file.absolutePath
}

/**
 * Function to save a bitmap image to the gallery.
 */
fun saveImageToGallery(context: Context, bitmap: Bitmap, title: String, description: String): String? {
    val contentValues = ContentValues().apply {
        put(MediaStore.Images.Media.DISPLAY_NAME, "$title.png")
        put(MediaStore.Images.Media.MIME_TYPE, "image/png")
        put(MediaStore.Images.Media.TITLE, title)
        put(MediaStore.Images.Media.DESCRIPTION, description)
        put(MediaStore.Images.Media.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
    }

    val uri = context.contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
    uri?.let {
        val outputStream: OutputStream? = context.contentResolver.openOutputStream(it)
        outputStream?.use { stream ->
            val buffer = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, buffer)
            stream.write(buffer.toByteArray())
        }
        return uri.toString()
    }
    return null
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