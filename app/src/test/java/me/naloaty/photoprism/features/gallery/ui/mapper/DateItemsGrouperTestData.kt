package me.naloaty.photoprism.features.gallery.ui.mapper

import java.text.SimpleDateFormat
import java.util.Calendar

class DateItemsGrouperTestData {

    var testGroups = listOf<Pair<String, List<TestCase>>>()

    // region: Single item
    val singleItem = listOf(
        date(year = 2024, month = 6, day = 1, hour = 10, minute = 10)
    )

    val singleItemTestCases = listOf(
        grouper(2, 2).test(
            singleItem to listOf(0, 1)
        ),
    ) registerAs "Single item"
    // endregion

    // region: Single per day within the same month items
    val singlePerDaySameMonthItems = buildList {
        for (day in 1..10) {
            add(date(year = 2024, month = 6, day = day, hour = 10, minute = 10))
        }
    }

    val singlePerDaySameMonthTestCases = listOf(
        grouper(1, 0).test(
            singlePerDaySameMonthItems to (0..10).toList()
        ),
        grouper(2, 0).test(
            singlePerDaySameMonthItems to (0..10).toList()
        ),
        grouper(3, 0).test(
            singlePerDaySameMonthItems to (0..10).toList()
        ),
        grouper(1, 1).test(
            singlePerDaySameMonthItems to (0..10).toList()
        ),
        grouper(2, 1).test(
            singlePerDaySameMonthItems to (0..10 step 2).toList()
        ),
        grouper(3, 1).test(
            singlePerDaySameMonthItems to (0..10 step 2).toList()
        ),
        grouper(1, 2).test(
            singlePerDaySameMonthItems to (0..10).toList()
        ),
        grouper(2, 2).test(
            singlePerDaySameMonthItems to (0..10 step 2).toList()
        ),
        grouper(3, 2).test(
            singlePerDaySameMonthItems to listOf(0, 3, 6, 9, 10)
        ),
    ) registerAs "Single per day within the same month items"
    //endregion

    // region: Variable per day within the same month items

    val variablePerDaySameMonthItems = buildList {
        for (day in 1..5) {
            for (minute in 10 until 10 + day) {
                add(date(year = 2024, month = 6, day = day, hour = 10, minute = minute))
            }
        }
    }

    val variablePerDaySameMonthTestCases = listOf(
        grouper(1, 0).test(
            variablePerDaySameMonthItems to listOf(0, 1, 3, 6, 10, 15)
        ),
        grouper(2, 0).test(
            variablePerDaySameMonthItems to listOf(0, 1, 3, 6, 10, 15)
        ),
        grouper(3, 0).test(
            variablePerDaySameMonthItems to listOf(0, 1, 3, 6, 10, 15)
        ),
        grouper(1, 1).test(
            variablePerDaySameMonthItems to listOf(0, 1, 3, 6, 10, 15)
        ),
        grouper(2, 1).test(
            variablePerDaySameMonthItems to listOf(0, 1, 3, 6, 10, 15)
        ),
        grouper(3, 1).test(
            variablePerDaySameMonthItems to listOf(0, 3, 6, 10, 15)
        ),
        grouper(6, 1).test(
            variablePerDaySameMonthItems to listOf(0, 3, 6, 10, 15)
        ),
        grouper(1, 2).test(
            variablePerDaySameMonthItems to listOf(0, 1, 3, 6, 10, 15)
        ),
        grouper(2, 2).test(
            variablePerDaySameMonthItems to listOf(0, 1, 3, 6, 10, 15)
        ),
        grouper(3, 2).test(
            variablePerDaySameMonthItems to listOf(0, 3, 6, 10, 15)
        ),
        grouper(6, 2).test(
            variablePerDaySameMonthItems to listOf(0, 6, 10, 15)
        ),
        grouper(7, 2).test(
            variablePerDaySameMonthItems to listOf(0, 6, 10, 15)
        ),
        grouper(11, 2).test(
            variablePerDaySameMonthItems to listOf(0, 6, 15)
        ),
        grouper(11, 3).test(
            variablePerDaySameMonthItems to listOf(0, 10, 15)
        ),
    ) registerAs "Variable per day within the same month items"
    // endregion

    // region: Single per day between months items
    val singlePerDayCrossMonthItems = listOf(
        // May 29 - 31
        date(year = 2024, month = 5, day = 29, hour = 11, minute = 10),
        date(year = 2024, month = 5, day = 30, hour = 11, minute = 10),
        date(year = 2024, month = 5, day = 31, hour = 11, minute = 10),
        // Next month; June 1 - 3
        date(year = 2024, month = 6, day = 1, hour = 11, minute = 10),
        date(year = 2024, month = 6, day = 2, hour = 11, minute = 10),
        date(year = 2024, month = 6, day = 3, hour = 11, minute = 10),
    )

    val singlePerDayCrossMonthTestCases = listOf(
        grouper(1, 0).test(
            singlePerDayCrossMonthItems to (0..6).toList()
        ),
        grouper(2, 0).test(
            singlePerDayCrossMonthItems to (0..6).toList()
        ),
        grouper(3, 0).test(
            singlePerDayCrossMonthItems to (0..6).toList()
        ),
        grouper(4, 0).test(
            singlePerDayCrossMonthItems to (0..6).toList()
        ),
        grouper(1, 1).test(
            singlePerDayCrossMonthItems to (0..6).toList()
        ),
        grouper(2, 1).test(
            singlePerDayCrossMonthItems to listOf(0, 2, 3, 5, 6)
        ),
        grouper(3, 1).test(
            singlePerDayCrossMonthItems to listOf(0, 2, 3, 5, 6)
        ),
        grouper(4, 1).test(
            singlePerDayCrossMonthItems to listOf(0, 2, 3, 5, 6)
        ),
        grouper(1, 2).test(
            singlePerDayCrossMonthItems to (0..6).toList()
        ),
        grouper(2, 2).test(
            singlePerDayCrossMonthItems to listOf(0, 2, 3, 5, 6)
        ),
        grouper(3, 2).test(
            singlePerDayCrossMonthItems to listOf(0, 3, 6)
        ),
        grouper(4, 2).test(
            singlePerDayCrossMonthItems to listOf(0, 3, 6)
        ),
        grouper(1, 3).test(
            singlePerDayCrossMonthItems to (0..6).toList()
        ),
        grouper(2, 3).test(
            singlePerDayCrossMonthItems to listOf(0, 2, 3, 5, 6)
        ),
        grouper(3, 3).test(
            singlePerDayCrossMonthItems to listOf(0, 3, 6)
        ),
        grouper(4, 3).test(
            singlePerDayCrossMonthItems to listOf(0, 3, 6)
        ),
    ) registerAs "Single per day between months items"
    // endregion

    // region: Variable per day between months items

    val variablePerDayCrossMonthItems = listOf(
        // May, 29
        date(year = 2024, month = 5, day = 29, hour = 11, minute = 10),
        // May, 30
        date(year = 2024, month = 5, day = 30, hour = 11, minute = 10),
        date(year = 2024, month = 5, day = 30, hour = 11, minute = 11),
        // May, 31
        date(year = 2024, month = 5, day = 31, hour = 11, minute = 10),
        date(year = 2024, month = 5, day = 31, hour = 11, minute = 11),
        date(year = 2024, month = 5, day = 31, hour = 11, minute = 12),
        // Next month; June, 1
        date(year = 2024, month = 6, day = 1, hour = 11, minute = 10),
        date(year = 2024, month = 6, day = 1, hour = 11, minute = 11),
        date(year = 2024, month = 6, day = 1, hour = 11, minute = 12),
        date(year = 2024, month = 6, day = 1, hour = 11, minute = 13),
        // June, 2
        date(year = 2024, month = 6, day = 2, hour = 11, minute = 10),
        date(year = 2024, month = 6, day = 2, hour = 11, minute = 11),
        date(year = 2024, month = 6, day = 2, hour = 11, minute = 12),
        date(year = 2024, month = 6, day = 2, hour = 11, minute = 13),
        date(year = 2024, month = 6, day = 2, hour = 11, minute = 14),
    )

    val variablePerDayCrossMonthTestCases = listOf(
        grouper(1, 0).test(
            variablePerDayCrossMonthItems to listOf(0, 1, 3, 6, 10, 15)
        ),
        grouper(2, 0).test(
            variablePerDayCrossMonthItems to listOf(0, 1, 3, 6, 10, 15)
        ),
        grouper(3, 1).test(
            variablePerDayCrossMonthItems to listOf(0, 3, 6, 10, 15)
        ),
        grouper(4, 1).test(
            variablePerDayCrossMonthItems to listOf(0, 3, 6, 10, 15)
        ),
        grouper(3, 2).test(
            variablePerDayCrossMonthItems to listOf(0, 3, 6, 10, 15)
        ),
        grouper(6, 2).test(
            variablePerDayCrossMonthItems to listOf(0, 6, 10, 15)
        ),
        grouper(11, 2).test(
            variablePerDayCrossMonthItems to listOf(0, 6, 15)
        ),
        grouper(20, 10).test(
            variablePerDayCrossMonthItems to listOf(0, 6, 15)
        )
    ) registerAs "Variable per day between months items"
    // endregion

    // region: Single per day between year items

    val singlePerDayCrossYearItems = listOf(
        date(year = 2023, month = 12, day = 29, hour = 11, minute = 10),
        date(year = 2023, month = 12, day = 30, hour = 11, minute = 10),
        date(year = 2023, month = 12, day = 31, hour = 11, minute = 10),
        date(year = 2024, month = 1, day = 1, hour = 11, minute = 10),
        date(year = 2024, month = 1, day = 2, hour = 11, minute = 10),
        date(year = 2024, month = 1, day = 3, hour = 11, minute = 10),
    )

    val singlePerDayCrossYearTestCases = listOf(
        grouper(1, 0).test(
            singlePerDayCrossYearItems to (0..6).toList()
        ),
        grouper(2, 0).test(
            singlePerDayCrossYearItems to (0..6).toList()
        ),
        grouper(3, 0).test(
            singlePerDayCrossYearItems to (0..6).toList()
        ),
        grouper(4, 0).test(
            singlePerDayCrossYearItems to (0..6).toList()
        ),
        grouper(1, 1).test(
            singlePerDayCrossYearItems to (0..6).toList()
        ),
        grouper(2, 1).test(
            singlePerDayCrossYearItems to listOf(0, 2, 3, 5, 6)
        ),
        grouper(3, 1).test(
            singlePerDayCrossYearItems to listOf(0, 2, 3, 5, 6)
        ),
        grouper(4, 1).test(
            singlePerDayCrossYearItems to listOf(0, 2, 3, 5, 6)
        ),
        grouper(1, 2).test(
            singlePerDayCrossYearItems to (0..6).toList()
        ),
        grouper(2, 2).test(
            singlePerDayCrossYearItems to listOf(0, 2, 3, 5, 6)
        ),
        grouper(3, 2).test(
            singlePerDayCrossYearItems to listOf(0, 3, 6)
        ),
        grouper(4, 2).test(
            singlePerDayCrossYearItems to listOf(0, 3, 6)
        ),
        grouper(1, 3).test(
            singlePerDayCrossYearItems to (0..6).toList()
        ),
        grouper(2, 3).test(
            singlePerDayCrossYearItems to listOf(0, 2, 3, 5, 6)
        ),
        grouper(3, 3).test(
            singlePerDayCrossYearItems to listOf(0, 3, 6)
        ),
        grouper(4, 3).test(
            singlePerDayCrossYearItems to listOf(0, 3, 6)
        ),
    ) registerAs "Single per day between year items"
    // endregion

    // region: Technical stuff

    private fun date(year: Int, month: Int, day: Int, hour: Int, minute: Int): Calendar {
        return Calendar.getInstance().apply {
            set(year, month - 1, day, hour, minute)
        }
    }

    fun List<Calendar>.formatAsClue(): String {
        val format = SimpleDateFormat("yyyy-MM-dd hh:mm")
        val formattedStrings = this.mapIndexed { index, calendar -> "\t[$index] ${format.format(calendar.time)}" }
        return "Items to groups:\n" + formattedStrings.joinToString("\n")
    }


    private fun grouper(orphansGroupSize: Int, orphansGroupDaysRange: Int): TestCase.GrouperParams {
        return TestCase.GrouperParams(orphansGroupSize, orphansGroupDaysRange)
    }

    private fun TestCase.GrouperParams.test(testCase: Pair<List<Calendar>, List<Int>>): TestCase {
        return TestCase(this, testCase.first, testCase.second)
    }

    private infix fun List<TestCase>.registerAs(name: String): List<TestCase> {
        testGroups += name to this
        return this
    }

    class TestCase(
        val params: GrouperParams,
        val itemsToGroup: List<Calendar>,
        val expectedGroups: List<Int>,
    ) {

        data class GrouperParams(
            val orphansGroupSize: Int,
            val orphansGroupDaysRange: Int,
        )
    }

    // endregion
}