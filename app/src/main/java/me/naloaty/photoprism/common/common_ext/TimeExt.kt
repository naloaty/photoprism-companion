package me.naloaty.photoprism.common.common_ext


import java.time.Instant
import java.util.Calendar
import java.util.Calendar.DAY_OF_YEAR
import java.util.Calendar.MONTH
import java.util.Calendar.YEAR
import kotlin.math.abs

fun Instant.toCalendar(): Calendar {
    return Calendar.getInstance().apply {
        timeInMillis = epochSecond * 1000
    }
}

fun Calendar.sameDayAs(other: Calendar): Boolean {
    return this[DAY_OF_YEAR] == other[DAY_OF_YEAR] && this.sameYearAs(other)
}

fun Calendar.sameMonthAs(other: Calendar): Boolean {
    return this[MONTH] == other[MONTH] && this.sameYearAs(other)
}

fun Calendar.sameYearAs(other: Calendar): Boolean {
    return this[YEAR] == other[YEAR]
}

fun Calendar.isCurrentYear(): Boolean {
    return this.sameYearAs(Calendar.getInstance())
}

fun Calendar.daysDiff(other: Calendar): Int {
    var dayOne = this.clone() as Calendar
    var dayTwo = other.clone() as Calendar

    if (dayOne.sameYearAs(dayTwo)) {
        return abs(dayOne[DAY_OF_YEAR] - dayTwo[DAY_OF_YEAR])
    }

    if (dayTwo[YEAR] > dayOne[YEAR]) {
        //swap them
        val temp = dayOne
        dayOne = dayTwo
        dayTwo = temp
    }

    var extraDays = 0
    val dayOneOriginalYearDays = dayOne[DAY_OF_YEAR]

    while (dayOne[YEAR] > dayTwo[YEAR]) {
        dayOne.add(YEAR, -1)
        // getActualMaximum() important for leap years
        extraDays += dayOne.getActualMaximum(DAY_OF_YEAR)
    }

    return extraDays - dayTwo[DAY_OF_YEAR] + dayOneOriginalYearDays
}