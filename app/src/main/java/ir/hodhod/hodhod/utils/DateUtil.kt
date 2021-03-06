package ir.hodhod.hodhod.utils

import java.text.SimpleDateFormat
import java.util.*

object DateUtil {
    fun formatTime(timeInMillis: Long): String {
        val dateFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
        return dateFormat.format(timeInMillis)
    }

    fun formatDate(timeInMillis: Long): String {
//        val dateFormat = SimpleDateFormat("MMMM dd, yyyy", Locale.getDefault())
        val dateFormat = SimpleDateFormat("dd/MM/yy", Locale.getDefault())
        return dateFormat.format(timeInMillis)
    }
}