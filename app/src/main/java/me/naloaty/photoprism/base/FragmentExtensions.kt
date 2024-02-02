package me.naloaty.photoprism.base

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.os.Build
import android.view.WindowInsets
import android.view.WindowManager
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel


inline fun <reified VM : ViewModel> BaseFragment.flowFragmentViewModels() = viewModels<VM> {
    val flowFragment = requireParentFragment().requireParentFragment() as BaseFlowFragment
    flowFragment.flowFragmentComponent.viewModelFactory()
}

inline fun <reified VM : ViewModel> BaseFlowFragment.flowFragmentViewModels() = viewModels<VM> {
    flowFragmentComponent.viewModelFactory()
}


inline fun <reified VM : ViewModel> BaseFragment.flowFragmentViewModel() = viewModels<VM>(
    ownerProducer = { requireParentFragment().requireParentFragment() }
)


fun Fragment.setupWindowInsets() {
    view?.let {
        it.setOnApplyWindowInsetsListener { _, windowInsets ->
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                val imeHeight = windowInsets.getInsets(WindowInsets.Type.ime()).bottom
                it.setPadding(0, 0, 0, imeHeight)
            }
            windowInsets
        }
    }
}

fun Fragment.setSoftInputMode(adjustPan: Boolean) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        requireActivity().window.setDecorFitsSystemWindows(adjustPan)
    } else {
        if (!adjustPan) {
            requireActivity().window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
        } else {
            requireActivity().window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
        }
    }
}

@SuppressLint("SourceLockedOrientationActivity")
fun Fragment.setPortraitOrientation() {
    requireActivity().requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
}

fun Fragment.setLandscapeOrientation() {
    requireActivity().requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
}

fun Fragment.setUnspecifiedOrientation() {
    requireActivity().requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
}