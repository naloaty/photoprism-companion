package me.naloaty.photoprism.features.media_viewer

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import me.naloaty.photoprism.common.common_paging.model.PagingState
import me.naloaty.photoprism.common.common_recycler.model.CommonEmptyItem
import me.naloaty.photoprism.common.common_recycler.model.CommonErrorItem
import me.naloaty.photoprism.common.common_recycler.model.CommonLoadingItem
import me.naloaty.photoprism.features.gallery_v2.presentation.list.GalleryState
import me.naloaty.photoprism.features.media_viewer.ui.model.PagerUiState


class PagerUiStateMapperTest : FunSpec({
    with(PagerUiStateMapperTestData()) {

        test("PagingState.EmptyProgress") {
            val state = GalleryState(listState = PagingState.EmptyProgress)
            val expectedState = PagerUiState(pagerItems = listOf(CommonLoadingItem))
            mapState(state) shouldBe expectedState
        }

        test("PagingState.Empty") {
            val state = GalleryState(listState = PagingState.Empty)
            mapState(state) shouldBe PagerUiState(pagerItems = listOf(CommonEmptyItem))
        }

        test("PagingState.EmptyError") {
            val state = GalleryState(listState = PagingState.EmptyError(someError))
            mapState(state) shouldBe PagerUiState(pagerItems = listOf(CommonErrorItem))
        }

        test("PagingState.Data") {
            val state = GalleryState(listState = PagingState.Data(someItems))
            mapState(state) shouldBe PagerUiState(pagerItems = someItemsViewTyped)
        }

        test("PagingState.Refresh") {
            val state = GalleryState(listState = PagingState.Refresh(someItems))
            mapState(state) shouldBe PagerUiState(pagerItems = someItemsViewTyped)
        }

        test("PagingState.NewPageProgress") {
            val state = GalleryState(listState = PagingState.NewPageProgress(someItems))
            mapState(state) shouldBe PagerUiState(pagerItems = someItemsViewTyped + CommonLoadingItem)
        }

        test("PagingState.NewPageError") {
            val state = GalleryState(listState = PagingState.NewPageError(someItems))
            mapState(state) shouldBe PagerUiState(pagerItems = someItemsViewTyped + CommonErrorItem)
        }

        test("PagingState.FullData") {
            val state = GalleryState(listState = PagingState.FullData(someItems))
            mapState(state) shouldBe PagerUiState(pagerItems = someItemsViewTyped)
        }
    }
})