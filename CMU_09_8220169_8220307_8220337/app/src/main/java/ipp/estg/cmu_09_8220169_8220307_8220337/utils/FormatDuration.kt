package ipp.estg.cmu_09_8220169_8220307_8220337.utils

fun formatDuration(duration: String): String {
    val parts = duration.split(":")
    return when (parts.size) {
        2 -> String.format("%02d:%02d", parts[0].toInt(), parts[1].toInt())
        3 -> String.format("%02d:%02d:%02d", parts[0].toInt(), parts[1].toInt(), parts[2].toInt())
        else -> "00:00"
    }
}