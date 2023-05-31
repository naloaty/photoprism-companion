package me.naloaty.photoprism

import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavOptions
import androidx.navigation.fragment.NavHostFragment
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import me.naloaty.photoprism.base.BaseActivity
import me.naloaty.photoprism.base.appViewModels
import me.naloaty.photoprism.features.auth.domain.model.LibraryAccount
import me.naloaty.photoprism.features.auth.domain.model.LibraryAccountSession
import me.naloaty.photoprism.navigation.main.model.SessionRenewState
import me.naloaty.photoprism.navigation.navigateSafely
import timber.log.Timber

class AppActivity : BaseActivity() {

    private val viewModel: AppViewModel by appViewModels()
    private val authViewModel: AuthViewModel by appViewModels()

    private val navController by lazy {
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment

        navHostFragment.navController
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        activityComponent.inject(this)
        observeAppState()
        observeSessionRenewal()
    }

    private fun observeSessionRenewal() {
        lifecycleScope.launch {
            authViewModel.onSessionRenew.collectLatest { bindSessionRenewState(it)}
        }
    }

    private fun bindSessionRenewState(state: SessionRenewState) {
        Timber.d("onSessionRenew: $state")

        when(state) {
            is SessionRenewState.RenewalError,
            is SessionRenewState.AccountNotSet -> {
                viewModel.launchAuthFlow()
            }
            is SessionRenewState.Success -> {
                viewModel.launchMainFlow(
                    account = state.account,
                    session = state.session
                )
            }
            is SessionRenewState.Renewing -> {
                viewModel.launchLoading()
            }
        }
    }

    private fun observeAppState() {
        lifecycleScope.launch {
            viewModel.appState.collectLatest { bindAppState(it) }
        }
    }

    private fun bindAppState(state: AppState) {
        when(state) {
            is AppState.Starting -> {
                Timber.d("Starting state")
            }

            is AppState.Main -> {
                Timber.d("Main state")
                navigateToMainFlow(
                    account = state.account,
                    session = state.session
                )
            }

            is AppState.Auth -> {
                Timber.d("Auth state")
                navigateToAuthFlow()
            }

            is AppState.Loading -> {
                Timber.d("Loading state")
                navigateToLoadingFragment()
            }
        }
    }

    private fun navigateToAuthFlow() {
        navController.navigateSafely(
            directions = NavGraphDirections.actionGlobalAuthFlow(),
            navOptions = NavOptions.Builder()
                .setPopUpTo(R.id.nav_graph, inclusive = true)
                .build()
        )
    }

    private fun navigateToMainFlow(account: LibraryAccount, session: LibraryAccountSession) {
        navController.navigateSafely(
            directions = NavGraphDirections.actionGlobalMainFlow(account, session),
            navOptions = NavOptions.Builder()
                .setPopUpTo(R.id.nav_graph, inclusive = true)
                .build()
        )
    }

    private fun navigateToLoadingFragment() {
        navController.navigateSafely(
            directions = NavGraphDirections.actionGlobalLoadingFragment(),
            navOptions = NavOptions.Builder()
                .setPopUpTo(R.id.nav_graph, inclusive = true)
                .build()
        )
    }
}