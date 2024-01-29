package me.naloaty.photoprism.features.auth.presentation

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import by.kirich1409.viewbindingdelegate.viewBinding
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import me.naloaty.photoprism.AppViewModel
import me.naloaty.photoprism.R
import me.naloaty.photoprism.base.BaseFragment
import me.naloaty.photoprism.base.flowFragmentViewModels
import me.naloaty.photoprism.databinding.FragmentLibraryConnectBinding
import me.naloaty.photoprism.features.auth.presentation.model.AuthState
import me.naloaty.photoprism.features.auth.presentation.model.bindToInputErrorState
import timber.log.Timber

class LibraryConnectFragment: BaseFragment(R.layout.fragment_library_connect) {

    private val viewModel: LibraryConnectViewModel by flowFragmentViewModels()
    private val viewBinding: FragmentLibraryConnectBinding by viewBinding()
    private val appViewModel: AppViewModel by activityViewModels()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupConnectButton()
        observeValidationErrors()
        observeAuthState()
        setValidationErrorsResetListeners()
    }

    private fun setupConnectButton() {
        viewBinding.btnConnect.setOnClickListener {
            viewModel.onPerformConnect(
                inputLibraryRoot = viewBinding.etLibraryRoot.text?.toString(),
                inputUsername = viewBinding.etUsername.text?.toString(),
                inputPassword = viewBinding.etPassword.text?.toString()
            )
        }
    }

    private fun observeValidationErrors() = with(viewLifecycleOwner) {
        lifecycleScope.launch {
            viewModel.errorInputLibraryRoot.collectLatest { state ->
                viewBinding.tilLibraryRoot.bindToInputErrorState(state)
            }
        }

        lifecycleScope.launch {
            viewModel.errorInputUsername.collectLatest { state ->
                viewBinding.tilUsername.bindToInputErrorState(state)
            }
        }

        lifecycleScope.launch {
            viewModel.errorInputPassword.collectLatest { state ->
                viewBinding.tilPassword.bindToInputErrorState(state)
            }
        }
    }

    private fun setValidationErrorsResetListeners() {
        viewBinding.etLibraryRoot.setOnFocusChangeListener { _, _ ->
            viewModel.resetErrorInputLibraryRoot()
        }

        viewBinding.etUsername.setOnFocusChangeListener { _, _ ->
            viewModel.resetErrorInputUsername()
        }

        viewBinding.etPassword.setOnFocusChangeListener { _, _ ->
            viewModel.resetErrorInputPassword()
        }
    }

    private fun observeAuthState() = with(viewLifecycleOwner){
        lifecycleScope.launch {
            viewModel.authState.collectLatest { state ->
                bindAuthState(state)
            }
        }
    }

    private fun bindAuthState(state: AuthState) {
        when(state) {
            is AuthState.None -> {
                Timber.d("Auth state is None")
                viewBinding.btnConnect.isVisible = true
                viewBinding.cpiConnecting.isVisible = false
            }

            is AuthState.Connecting -> {
                Timber.d("Auth state is Connecting")
                viewBinding.btnConnect.isVisible = false
                viewBinding.cpiConnecting.isVisible = true
                viewModel.resetErrorInputLibraryRoot()
                viewModel.resetErrorInputUsername()
                viewModel.resetErrorInputPassword()
            }

            is AuthState.Authenticated -> {
                Timber.d("Auth state is $state")
                appViewModel.launchMainFlow(
                    account = state.account,
                    session = state.session
                )
                Timber.d(state.session.toString())
            }
        }
    }

}