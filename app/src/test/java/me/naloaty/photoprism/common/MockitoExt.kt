package me.naloaty.photoprism.common

import org.mockito.Mockito

inline fun <reified T> stub(): T = Mockito.mock(T::class.java)