package ipp.estg.cmu_09_8220169_8220307_8220337.utils

fun formatDuration(duration: String): String {
    val totalSeconds = duration.toIntOrNull() ?: return "00:00:00"
    val hours = totalSeconds / 3600
    val minutes = (totalSeconds % 3600) / 60
    val seconds = totalSeconds % 60
    return String.format("%02d:%02d:%02d", hours, minutes, seconds)
}