package me.naloaty.photoprism.navigation.main

import android.os.Bundle
import android.view.View
import me.naloaty.photoprism.R
import me.naloaty.photoprism.base.BaseFlowFragment
import me.naloaty.photoprism.base.setSoftInputAdjustResize

class DetailViewFlowFragment : BaseFlowFragment(
    R.layout.flow_fragment_detail_view, R.id.nav_host_fragment_detail_view
) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setSoftInputAdjustResize(view)
    }
}