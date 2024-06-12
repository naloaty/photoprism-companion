package me.naloaty.photoprism.features.gallery.ui.mapper

import me.naloaty.photoprism.common.common_ext.daysDiff
import me.naloaty.photoprism.common.common_ext.sameDayAs
import me.naloaty.photoprism.common.common_ext.sameMonthAs
import me.naloaty.photoprism.common.common_ext.sameYearAs
import java.util.Calendar
import java.util.LinkedList

class DateItemsGrouper<T>(
    private val orphansGroupSize: Int = 6,
    private val orphansGroupDaysRange: Int = 5,
    private val dateSelector: (T) -> Calendar
) {

    /**
     * DO NOT USE THIS OVERLOAD - USE CALLBACK VERSION INSTEAD.
     *
     * Groups items by same day. However, if items are arranged in a such way that:
     * - they belong to a day with a number of items < [orphansGroupSize];
     * - amount of days between them are less or equal to [orphansGroupDaysRange];
     * - they are within the same month;
     * - they are within the same year;
     *
     * then they are grouped by [orphansGroupSize] items in each group.
     *
     * @return A list of indices of the beginnings of each group.
     *         Last item is index (exclusive) of the end of the last group.
     */
    fun groupByDate(items: List<T>): List<Int> {
        if (items.isEmpty()) return emptyList()
        if (items.size == 1) return listOf(0, 1)

        val groups = LinkedList<Int>().apply { addFirst(0) }
        var prevDate = dateSelector(items.first())

        for (index in 1 until items.size) {
            val currDate = dateSelector(items[index])

            if (!currDate.sameDayAs(prevDate)) {
                groups += index
            }

            prevDate = currDate
        }

        groups += items.size

        val iterator = groups.listIterator()
        var firstGroupStartIndex: Int
        var secondGroupStartIndex = iterator.next()
        var secondGroupEndIndex = iterator.next()

        while (iterator.hasNext()) {
            firstGroupStartIndex = secondGroupStartIndex
            secondGroupStartIndex = secondGroupEndIndex
            secondGroupEndIndex = iterator.next()

            val firstGroupSize = secondGroupStartIndex - firstGroupStartIndex
            val secondGroupSize = secondGroupEndIndex - secondGroupStartIndex
            val firstGroupStart = dateSelector(items[firstGroupStartIndex])
            val secondGroupStart = dateSelector(items[secondGroupEndIndex - 1])

            when {
                firstGroupSize + secondGroupSize > orphansGroupSize -> continue
                !firstGroupStart.sameMonthAs(secondGroupStart) -> continue
                !firstGroupStart.sameYearAs(secondGroupStart) -> continue
                firstGroupStart.daysDiff(secondGroupStart) > orphansGroupDaysRange -> continue
            }

            // Iterator currently located "after" the secondGroupEndIndex,
            // thus we need to go "after" secondGroupStartIndex position and only then
            // get its value by second previous() call.
            repeat(2) { iterator.previous() }

            iterator.remove()
            secondGroupStartIndex = iterator.previous()
            iterator.next()
            secondGroupEndIndex = iterator.next()
        }

        return groups
    }

    /**
     * Groups items by same day and invokes callback for each group
     * with start (inclusive) and end (exclusive) indices. Callback
     * invokes are guaranteed to be in ascending index order.
     */
    inline fun groupByDate(items: List<T>, callback: (start: Int, end: Int) -> Unit) {
        // TODO: This implementation has O(3N) complexity for now. O(N) will be implemented in the future.
        val iterator = groupByDate(items).iterator()
        if (!iterator.hasNext()) return

        var start = iterator.next()
        var end: Int

        while (iterator.hasNext()) {
            end = iterator.next()
            callback(start, end)
            start = end
        }
    }
}