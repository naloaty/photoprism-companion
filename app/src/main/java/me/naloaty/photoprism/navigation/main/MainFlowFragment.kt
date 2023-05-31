package me.naloaty.photoprism.navigation.main

import android.os.Bundle
import androidx.annotation.AnimRes
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.ui.setupWithNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import kotlinx.coroutines.flow.collectLatest
import me.naloaty.photoprism.R
import me.naloaty.photoprism.base.BaseActivity
import me.naloaty.photoprism.base.BaseFlowFragment
import me.naloaty.photoprism.common.extension.setOnAnimationEndListener
import me.naloaty.photoprism.common.extension.startAnimation
import me.naloaty.photoprism.databinding.FlowFragmentMainBinding
import me.naloaty.photoprism.navigation.main.model.ViewState

class MainFlowFragment : BaseFlowFragment(
    R.layout.flow_fragment_main, R.id.nav_host_fragment_main
) {

    private val args: MainFlowFragmentArgs by navArgs()
    private val bottomNavViewModel: BottomNavViewModel by viewModels()
    private val viewBinding: FlowFragmentMainBinding by viewBinding()

    val sessionComponent by lazy {
        (requireActivity() as BaseActivity)
            .activityComponent
            .sessionComponentFactory()
            .create(
                account = args.account,
                session = args.session
            )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        flowFragmentComponent.inject(this)
    }

    override fun onStart() {
        super.onStart()
        setupBottomNavigation()
    }

    override fun setupNavigation(navController: NavController) = with(viewBinding) {
        bottomNavigation.setupWithNavController(navController)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.album_content_fragment -> bottomNavViewModel.onAlbumContentNavigated()
                R.id.albums_fragment -> bottomNavViewModel.onAlbumsNavigated()
            }
        }
    }

    private fun setupBottomNavigation() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            bottomNavViewModel.bottomNavigationState.collectLatest { state ->
                when (state) {
                    is ViewState.Shown -> showBottomNavigation(state.animResId)
                    is ViewState.Hidden -> hideBottomNavigation(state.animResId)
                }
            }
        }
    }


    private fun hideBottomNavigation(@AnimRes animResId: Int?) = with(viewBinding) {
        if (!bottomNavigation.isVisible)
            return

        if (animResId != null) {
            bottomNavigation.startAnimation(animResId) {
                setOnAnimationEndListener {
                    viewBinding.bottomNavigation.isVisible = false
                }
            }
        } else {
            viewBinding.bottomNavigation.isVisible = false
        }
    }

    private fun showBottomNavigation(@AnimRes animResId: Int?) = with(viewBinding) {
        if (bottomNavigation.isVisible)
            return

        if (animResId != null) {
            bottomNavigation.startAnimation(animResId) {
                setOnAnimationEndListener {
                    viewBinding.bottomNavigation.isVisible = true
                }
            }
        } else {
            viewBinding.bottomNavigation.isVisible = true
        }
    }

}