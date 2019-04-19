package io.intrepid.skotlinton.testutils

import io.intrepid.skotlinton.di.TestApplicationModule
import io.intrepid.skotlinton.di.TestNetworkingModule
import org.junit.After
import org.junit.Rule
import org.mockito.junit.MockitoJUnit
import org.mockito.junit.MockitoRule

abstract class BaseUiTest {
    @Rule
    @JvmField
    val mockitoRule: MockitoRule = MockitoJUnit.rule()

    @After
    fun tearDown() {
        TestApplicationModule.reset()
        TestNetworkingModule.reset()
    }
}
