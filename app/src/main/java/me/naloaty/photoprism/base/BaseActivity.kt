package me.naloaty.photoprism.base

import androidx.fragment.app.FragmentActivity
import me.naloaty.photoprism.App

abstract class BaseActivity : FragmentActivity() {

    val activityComponent by lazy {
        (application as App)
            .appComponent
            .activityComponentFactory()
            .create(
                activity = this
            )
    }
}