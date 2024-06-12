package me.naloaty.photoprism.features.gallery.ui.mapper

import android.text.format.DateFormat
import me.naloaty.photoprism.common.common_ext.isCurrentYear
import me.naloaty.photoprism.common.common_ext.sameDayAs
import me.naloaty.photoprism.common.common_ext.sameMonthAs
import me.naloaty.photoprism.common.common_ext.toCalendar
import me.naloaty.photoprism.features.gallery.ui.mapper.ItemsGroupTitleBuilder.Template.SYSTEM
import me.naloaty.photoprism.features.gallery.ui.mapper.ItemsGroupTitleBuilder.Template.SYSTEM_SHORT
import me.naloaty.photoprism.features.gallery.ui.mapper.ItemsGroupTitleBuilder.Template.SYSTEM_SHORT_WEEK_DAY
import me.naloaty.photoprism.features.gallery.ui.mapper.ItemsGroupTitleBuilder.Template.SYSTEM_WEEK_DAY
import java.text.SimpleDateFormat
import java.time.Instant
import java.util.Calendar
import java.util.Locale
import java.util.Locale.Category

class ItemsGroupTitleBuilder(template: Template) {

    companion object {
        val defaultTemplate = SYSTEM_SHORT
    }

    enum class Template {
        SYSTEM,
        SYSTEM_SHORT,
        SYSTEM_WEEK_DAY,
        SYSTEM_SHORT_WEEK_DAY,
    }

    private val locale: Locale = Locale.getDefault(Category.FORMAT)

    private val formatRule: RangeFormatRule = when (template) {
        SYSTEM -> BestPatternFormatRule(locale, "MMMMdyyyy")
        SYSTEM_SHORT -> BestPatternFormatRule(locale, "MMMdyyyy")
        SYSTEM_WEEK_DAY -> BestPatternFormatRule(locale, "EEMMMMdyyyy")
        SYSTEM_SHORT_WEEK_DAY -> BestPatternFormatRule(locale, "EEMMMdyyyy")
    }

    fun create(date: Instant): String {
        return create(date, date)
    }

    fun create(startDate: Instant, endDate: Instant): String {
        return formatRule.format(startDate.toCalendar(), endDate.toCalendar())
    }
}

private interface RangeFormatRule {

    fun format(first: Calendar, second: Calendar): String
}

private class BestPatternFormatRule(
    locale: Locale,
    pattern: String,
) : RangeFormatRule {

    private val formatter = ContextEscapedBestPatternFormatter(
        locale, pattern, 'd'
    )

    private val useShortRange = run {
        val shortRangeSet = setOf('d', 'y', 'M')
        pattern.all { it in shortRangeSet }
    }

    override fun format(first: Calendar, second: Calendar): String {
        return when {
            first.sameDayAs(second) -> {
                formatter.format(first)
            }
            first.sameMonthAs(second) && useShortRange -> {
                val f = formatter.formatTarget(first)
                    ?: return formatter.format(first)

                val s = formatter.formatTarget(second)
                    ?: return formatter.format(second)

                formatter.formatWithPayload(first, "$f–$s")
            }
            else -> {
                formatter.format(first) + " – " + formatter.format(second)
            }
        }
    }

}

private class ContextEscapedBestPatternFormatter(
    locale: Locale,
    pattern: String,
    escapeTarget: Char
) {

    private val currentYearFormatter = EscapedBestPatternFormatter(
        locale, pattern.replace('y', Char.MIN_VALUE), escapeTarget
    )

    private val pastYearFormatter = EscapedBestPatternFormatter(
        locale, pattern, escapeTarget
    )

    fun formatTarget(date: Calendar): String? {
        return currentYearFormatter.formatTarget(date)
    }

    fun formatWithPayload(date: Calendar, payload: String): String {
        return if (date.isCurrentYear()) {
            currentYearFormatter.formatWithPayload(date, payload)
        } else {
            pastYearFormatter.formatWithPayload(date, payload)
        }
    }

    fun format(date: Calendar): String {
        return if (date.isCurrentYear()) {
            currentYearFormatter.format(date)
        } else {
            pastYearFormatter.format(date)
        }
    }

}

private class EscapedBestPatternFormatter(
    locale: Locale,
    pattern: String,
    escapeTarget: Char
) {
    private val bestPattern = DateFormat.getBestDateTimePattern(locale, pattern)
    private val targetPattern = bestPattern.filter { it == escapeTarget }

    private val targetFound = targetPattern.isNotEmpty()
    private val bestFormat = SimpleDateFormat(bestPattern, locale)

    private val targetFormat by lazy {
        SimpleDateFormat(targetPattern, locale)
    }

    private val escapedFormat by lazy {
        SimpleDateFormat(bestPattern.replace(escapeTarget.toString(), "'$targetPattern'"), locale)
    }

    fun formatTarget(date: Calendar): String? {
        return if (targetFound) targetFormat.format(date.time) else null
    }

    fun formatWithPayload(date: Calendar, payload: String): String {
        val escaped = escapedFormat.format(date.time)

        return if (targetFound) {
            escaped.replace(targetPattern, payload)
        } else {
            escaped
        }
    }

    fun format(date: Calendar): String {
        return bestFormat.format(date.time)
    }
}


