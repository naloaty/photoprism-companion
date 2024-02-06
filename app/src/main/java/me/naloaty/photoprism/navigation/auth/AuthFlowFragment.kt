package me.naloaty.photoprism.navigation.auth

import android.os.Bundle
import android.view.View
import me.naloaty.photoprism.R
import me.naloaty.photoprism.base.BaseFlowFragment
import me.naloaty.photoprism.base.setSoftInputAdjustNothing

class AuthFlowFragment : BaseFlowFragment(
    R.layout.flow_fragment_auth, R.id.nav_host_fragment_auth
) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setSoftInputAdjustNothing(view)
    }
}