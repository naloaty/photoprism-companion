package me.naloaty.photoprism.features.common_recycler

import androidx.annotation.MainThread
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.conflate

@MainThread
fun RecyclerView.pagingEndlessScrollFlow(): Flow<Int> = callbackFlow {
    val listener = EndlessScrollListenerImpl(
        layoutManager = this@pagingEndlessScrollFlow.layoutManager as LinearLayoutManager,
    ) { position ->
        trySend(position)
    }
    addOnScrollListener(listener)
    awaitClose { removeOnScrollListener(listener) }
}
    .conflate()


private class EndlessScrollListenerImpl(
    private val layoutManager: LinearLayoutManager,
    private val loadMoreCallback: (Int) -> Unit
) : RecyclerView.OnScrollListener() {
    private var previousFirstVisibleItem = 0
    private var previousLastVisibleItem = 0

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        if (dy < 0) {
            previousFirstVisibleItem = layoutManager.findFirstVisibleItemPosition().also {
                if (previousFirstVisibleItem != it) {
                    loadMoreCallback(it)
                }
            }
        } else {
            previousLastVisibleItem = layoutManager.findLastVisibleItemPosition().also {
                if (previousLastVisibleItem != it) {
                    loadMoreCallback(it)
                }
            }
        }
    }
}