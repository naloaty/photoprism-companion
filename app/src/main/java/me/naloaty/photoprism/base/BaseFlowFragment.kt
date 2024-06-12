package me.naloaty.photoprism.base

import android.os.Bundle
import android.view.View
import androidx.annotation.IdRes
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import me.naloaty.photoprism.di.activity.ActivityComponent
import me.naloaty.photoprism.di.injector.Injector

abstract class BaseFlowFragment(
    @LayoutRes layoutId: Int,
    @IdRes private val navHostFragmentId: Int
) : Fragment(layoutId) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (arguments == null) {
            arguments = Bundle()
        }
    }

    val flowFragmentComponent by lazy {
        Injector.get(ActivityComponent::class.java)
            .flowFragmentComponentFactory()
            .create(fragment = this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val navHostFragment = childFragmentManager
            .findFragmentById(navHostFragmentId) as NavHostFragment

        val navController = navHostFragment.navController
        setupNavigation(navController)
    }

    protected open fun setupNavigation(navController: NavController) = Unit
}