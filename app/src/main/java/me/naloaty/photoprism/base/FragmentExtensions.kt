package me.naloaty.photoprism.base

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.os.Build
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsAnimationCompat
import androidx.core.view.WindowInsetsAnimationCompat.BoundsCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsCompat.Type
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import kotlin.math.roundToInt


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

fun Fragment.setSoftInputAdjustResize(root: View) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        requireActivity().window.setDecorFitsSystemWindows(false)
        root.setOnApplyWindowInsetsListener { _, insets ->
            val imeHeight = insets.getInsets(WindowInsets.Type.ime()).bottom
            val statusHeight = insets.getInsets(WindowInsets.Type.systemBars()).top
            val navHeight = insets.getInsets(WindowInsets.Type.systemBars()).bottom
            val bottomHeight = if (imeHeight == 0) navHeight else 0

            root.setPadding(0, statusHeight, 0, bottomHeight)
            WindowInsets.CONSUMED
        }

        ViewCompat.setWindowInsetsAnimationCallback(
            root,
            object : WindowInsetsAnimationCompat.Callback(DISPATCH_MODE_STOP) {
                var endBottomPadding = 0
                var initialBottomPadding = 0

                override fun onStart(
                    animation: WindowInsetsAnimationCompat,
                    bounds: BoundsCompat
                ): BoundsCompat {
                    endBottomPadding = bounds.upperBound.bottom
                    initialBottomPadding = root.paddingBottom
                    return bounds
                }

                override fun onProgress(
                    insets: WindowInsetsCompat,
                    runningAnimations: MutableList<WindowInsetsAnimationCompat>
                ): WindowInsetsCompat {
                    // Find an IME animation.
                    val imeAnimation = runningAnimations.find {
                        it.typeMask and Type.ime() != 0
                    } ?: return insets

                    if (initialBottomPadding != 0) return insets
                    val currPadding = endBottomPadding * imeAnimation.interpolatedFraction

                    with(root) {
                        setPadding(paddingLeft, paddingTop, paddingRight, currPadding.roundToInt())
                    }
                    return insets
                }
            }
        )
    } else {
        requireActivity().window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
    }
}

fun Fragment.setSoftInputAdjustPan() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        requireActivity().window.setDecorFitsSystemWindows(true)
    }
    requireActivity().window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
}

fun Fragment.setSoftInputAdjustNothing(root: View) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        requireActivity().window.setDecorFitsSystemWindows(true)
        ViewCompat.setWindowInsetsAnimationCallback(root, null)
    }
    requireActivity().window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING)
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