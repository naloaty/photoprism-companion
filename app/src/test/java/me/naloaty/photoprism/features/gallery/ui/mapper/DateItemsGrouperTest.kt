package me.naloaty.photoprism.features.gallery.ui.mapper

import io.kotest.assertions.withClue
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import java.util.Calendar

class DateItemsGrouperTest : BehaviorSpec({
    with(DateItemsGrouperTestData()) {

        Given("DateItemsGrouperTest") {
            testGroups.forEach { (name, testCases) ->
                When(name.lowercase()) {
                    testCases.forEach { testCase ->
                        val groupSize = testCase.params.orphansGroupSize
                        val groupDaysRange = testCase.params.orphansGroupDaysRange

                        And("orphansGroupSize = $groupSize, orphansGroupDaysRange = $groupDaysRange") {
                            val grouper = DateItemsGrouper<Calendar>(
                                orphansGroupSize = groupSize,
                                orphansGroupDaysRange = groupDaysRange,
                                dateSelector = { it }
                            )

                            Then("items are grouped by a day or range (orphans)") {
                                withClue(testCase.itemsToGroup.formatAsClue()) {
                                    grouper.groupByDate(testCase.itemsToGroup) shouldBe testCase.expectedGroups
                                }
                            }
                        }
                    }
                }
            }
        }
    }
})