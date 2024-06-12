package me.naloaty.photoprism.di.viewmodel

import androidx.lifecycle.ViewModel
import com.yandex.yatagan.IntoMap
import kotlin.reflect.KClass

@Target(
    AnnotationTarget.FUNCTION,
    AnnotationTarget.PROPERTY_GETTER,
    AnnotationTarget.PROPERTY_SETTER
)
@IntoMap.Key
annotation class ViewModelKey(val value: KClass<out ViewModel>)