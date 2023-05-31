package me.naloaty.photoprism.common.extension

import android.view.View
import android.view.animation.Animation
import android.view.animation.Animation.AnimationListener
import android.view.animation.AnimationUtils
import androidx.annotation.AnimRes

inline fun Animation.setOnAnimationEndListener(
    crossinline listener: (animation: Animation) -> Unit
) {
    this.setAnimationListener(object : AnimationListener {
        override fun onAnimationStart(animation: Animation?) {}

        override fun onAnimationEnd(animation: Animation?) {
            listener.invoke(this@setOnAnimationEndListener)
        }

        override fun onAnimationRepeat(animation: Animation?) {}
    })
}

fun View.startAnimation(@AnimRes animResId: Int) {
    this.startAnimation(
        AnimationUtils.loadAnimation(this.context, animResId)
    )
}

inline fun View.startAnimation(@AnimRes animResId: Int, config: Animation.() -> Unit) {
    this.startAnimation(
        AnimationUtils.loadAnimation(this.context, animResId).apply(config)
    )
}
