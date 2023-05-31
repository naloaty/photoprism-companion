package me.naloaty.photoprism.base

import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import me.naloaty.photoprism.navigation.main.MainFlowFragment

abstract class BaseSessionFragment(@LayoutRes layoutId: Int): Fragment(layoutId) {

    val fragmentWithSessionComponent by lazy {
        val flowFragment = requireParentFragment().requireParentFragment()

        (flowFragment as MainFlowFragment)
            .sessionComponent
            .fragmentWithSessionComponentFactory()
            .create()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (arguments == null) {
            arguments = Bundle()
        }
    }

}