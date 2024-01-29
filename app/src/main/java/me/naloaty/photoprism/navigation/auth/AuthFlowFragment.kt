package me.naloaty.photoprism.navigation.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import me.naloaty.photoprism.R
import me.naloaty.photoprism.base.BaseFlowFragment

class AuthFlowFragment : BaseFlowFragment(
    R.layout.flow_fragment_auth, R.id.nav_host_fragment_auth
) {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setupSoftInputMode()
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    private fun setupSoftInputMode() {
        requireActivity().window.setSoftInputMode(
            WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING
        )
    }
}