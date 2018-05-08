package com.fleenmobile.destinationcompass

import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.plugins.RxJavaPlugins
import org.junit.After
import org.junit.Before
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

abstract class BaseTest {

    @Before
    open fun setup() = MockitoAnnotations.initMocks(this)

    @After
    open fun tearDown() {
        RxAndroidPlugins.reset()
        RxJavaPlugins.reset()
    }

    protected fun <T> any(): T {
        Mockito.any<T>()
        return uninitialized()
    }

    protected fun <T> any(c: Class<T>): T {
        Mockito.any<T>()
        return uninitialized()
    }

    private fun <T> uninitialized(): T = null as T
}