package me.naloaty.photoprism.features.media_viewer.ui.mapper

import android.text.format.DateFormat
import me.naloaty.photoprism.common.common_ext.toCalendar
import java.text.SimpleDateFormat
import java.time.Instant
import java.util.Locale
import java.util.Locale.Category
import javax.inject.Inject

class PagerItemDateBuilder @Inject constructor() {

    private val locale: Locale = Locale.getDefault(Category.FORMAT)

    private val formatter = SimpleDateFormat(
        DateFormat.getBestDateTimePattern(locale, "EEMMMMdyyyy HH:mm"),
        locale
    )

    fun create(date: Instant): String {
        return formatter.format(date.toCalendar().time)
    }

}