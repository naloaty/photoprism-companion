package me.naloaty.photoprism.base

import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment

abstract class BaseFragment(@LayoutRes layoutId: Int): Fragment(layoutId) {

    val fragmentComponent by lazy {
        val flowFragment = requireParentFragment().requireParentFragment()

        (flowFragment as BaseFlowFragment)
            .flowFragmentComponent
            .fragmentComponentFactory()
            .create()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (arguments == null) {
            arguments = Bundle()
        }
    }

}