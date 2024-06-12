package me.naloaty.photoprism.common.common_recycler

import androidx.annotation.MainThread
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.conflate
import kotlin.math.abs

@MainThread
fun RecyclerView.pagingEndlessScrollFlow(pageSize: Int = 10): Flow<Int> = callbackFlow {
    val listener = EndlessScrollListenerImpl(
        layoutManager = this@pagingEndlessScrollFlow.layoutManager as LinearLayoutManager,
        pageSize = pageSize
    ) { position ->
        trySend(position)
    }
    addOnScrollListener(listener)
    awaitClose { removeOnScrollListener(listener) }
}
    .conflate()


private class EndlessScrollListenerImpl(
    private val layoutManager: LinearLayoutManager,
    private val pageSize: Int,
    private val loadMoreCallback: (Int) -> Unit
) : RecyclerView.OnScrollListener() {
    private var previousFirstVisibleItem = 0
    private var previousLastVisibleItem = 0

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        if (dy < 0) {
            val firstVisibleItem = layoutManager.findFirstVisibleItemPosition()
            if (abs(firstVisibleItem - previousFirstVisibleItem) >= pageSize) {
                loadMoreCallback(firstVisibleItem)
                previousFirstVisibleItem = firstVisibleItem
            }
        } else {
            val lastVisibleItem = layoutManager.findLastVisibleItemPosition()
            if (abs(lastVisibleItem - previousLastVisibleItem) >= pageSize) {
                loadMoreCallback(lastVisibleItem)
                previousLastVisibleItem = lastVisibleItem
            }
        }
    }
}