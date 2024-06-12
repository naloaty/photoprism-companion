package me.naloaty.photoprism

import me.naloaty.photoprism.features.gallery.ui.mapper.DateItemsGrouper
import java.util.Calendar

fun main() {
    val grouper = DateItemsGrouper<Calendar>(2, 1, { it })
    val singlePerDaySameMonthItems = buildList {
        for (day in 1..10) {
            add(date(year = 2024, month = 6, day = day, hour = 10, minute = 10))
        }
    }
    println(grouper.groupByDate(singlePerDaySameMonthItems))
//
//    val list = LinkedList<Int>()
//    list.add(1)
//    list.add(2)
//    list.add(3)
//    val iter = list.listIterator()
//    println(iter.next())
//    println(iter.next())
//    println(iter.next())
//    println(iter.previous())
//    println(iter.previous())
//    println(iter.previous())
}

private fun date(year: Int, month: Int, day: Int, hour: Int, minute: Int): Calendar {
    return Calendar.getInstance().apply {
        set(year, month - 1, day, hour, minute)
    }
}