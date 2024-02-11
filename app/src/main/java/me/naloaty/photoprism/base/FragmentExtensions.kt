package me.naloaty.photoprism.base

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.os.Build
import android.view.View
import android.view.WindowManager
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsAnimationCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsCompat.Type
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel



inline fun <reified VM : ViewModel> BaseFragment.fragmentViewModels() = viewModels<VM> {
    val flowFragment = requireParentFragment().requireParentFragment() as BaseFlowFragment
    flowFragment.flowFragmentComponent.viewModelFactory()
}

inline fun <reified VM : ViewModel> BaseFragment.flowFragmentViewModels() = viewModels<VM>(
    ownerProducer = { requireParentFragment().requireParentFragment() }
){
    val flowFragment = requireParentFragment().requireParentFragment() as BaseFlowFragment
    flowFragment.flowFragmentComponent.viewModelFactory()
}

fun Fragment.setSoftInputAdjustResize(root: View) {
    /* data */ class InsetsHolder(
        var imeHeight: Int = 0,
        var navBarHeight: Int = 0,
    )

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        requireActivity().window.setDecorFitsSystemWindows(false);
    } else {
        requireActivity().window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )
    }

    val insetsHolder = InsetsHolder()

    ViewCompat.setOnApplyWindowInsetsListener(root) { _, insets ->
        val imeHeight = insets.getInsets(Type.ime()).bottom
        val statusHeight = insets.getInsets(Type.statusBars()).top
        val navBarHeight = insets.getInsets(Type.navigationBars()).bottom

        val softKeyboardIsVisible = insets.isVisible(Type.ime())
        val bottomHeightIsSet = root.paddingBottom == imeHeight

        /**
         * Sometimes reported ime height is wrong (much smaller),
         * so we need a little "hack" to detect & fix that.
         *
         * Note: keyboard window is also glitches when it happens
         * (because wrong end animation height, i guess), so it must be not my fault.
         */
        val bottomHeightIsOff = root.paddingBottom < imeHeight
                && imeHeight > 0 && root.paddingBottom > navBarHeight

        /**
         * Keyboard is changed (e.g. samsung -> gboard)
         */
        val bottomHeightIsOff2 = insetsHolder.imeHeight > navBarHeight &&
                insetsHolder.imeHeight != imeHeight

        val bottomHeight = when {
            softKeyboardIsVisible && bottomHeightIsSet -> {
                imeHeight
            }
            softKeyboardIsVisible && (bottomHeightIsOff || bottomHeightIsOff2) -> {
                imeHeight
            }
            else -> navBarHeight
        }

        insetsHolder.apply {
            this.imeHeight = imeHeight
            this.navBarHeight = navBarHeight
        }

//        Timber.d(buildString {
//            appendLine("Context: Apply Insets")
//            appendLine(insetsHolder)
//            appendLine("paddingBottom = ${root.paddingBottom}")
//            appendLine("softKeyboardIsVisible = $softKeyboardIsVisible")
//            appendLine("bottomHeightIsSet = $bottomHeightIsSet")
//            appendLine("bottomHeightIsOff = $bottomHeightIsOff")
//            appendLine("bottomHeight = $bottomHeight")
//            appendLine("============")
//        })

        root.setPadding(root.paddingLeft, statusHeight, root.paddingRight, bottomHeight)
        WindowInsetsCompat.CONSUMED
    }

    ViewCompat.setWindowInsetsAnimationCallback(
        root,
        object : WindowInsetsAnimationCompat.Callback(DISPATCH_MODE_STOP) {
            override fun onProgress(
                insets: WindowInsetsCompat,
                runningAnimations: MutableList<WindowInsetsAnimationCompat>
            ): WindowInsetsCompat {
                if (insetsHolder.imeHeight == 0) return insets
                val currentImeHeight = insets.getInsets(Type.ime()).bottom

//                Timber.d(buildString {
//                    appendLine("Context: Animation")
//                    appendLine(insetsHolder)
//                    appendLine("currentImeHeight = $currentImeHeight")
//                    appendLine("============")
//                })

                with(root) {
                    setPadding(paddingLeft, paddingTop, paddingRight, currentImeHeight)
                }

                return insets
            }
        }
    )
}

fun Fragment.setSoftInputAdjustPan(root: View) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        requireActivity().window.setDecorFitsSystemWindows(true)
    } else {
        requireActivity().window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_ATTACHED_IN_DECOR,
            WindowManager.LayoutParams.FLAG_LAYOUT_ATTACHED_IN_DECOR
        )
    }

    ViewCompat.setWindowInsetsAnimationCallback(root, null)
    ViewCompat.setOnApplyWindowInsetsListener(root, null)

    requireActivity().window.setSoftInputMode(
        WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN
    )
}

fun Fragment.setSoftInputAdjustNothing(root: View) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        requireActivity().window.setDecorFitsSystemWindows(true)
    }

    ViewCompat.setWindowInsetsAnimationCallback(root, null)
    ViewCompat.setOnApplyWindowInsetsListener(root, null)

    requireActivity().window.setSoftInputMode(
        WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING
    )
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