package me.naloaty.photoprism.navigation.auth

import me.naloaty.photoprism.R
import me.naloaty.photoprism.base.BaseFlowFragment
import me.naloaty.photoprism.base.setPortraitOrientation
import me.naloaty.photoprism.base.setSoftInputMode
import me.naloaty.photoprism.base.setUnspecifiedOrientation
import me.naloaty.photoprism.base.setupWindowInsets

class AuthFlowFragment : BaseFlowFragment(
    R.layout.flow_fragment_auth, R.id.nav_host_fragment_auth
)