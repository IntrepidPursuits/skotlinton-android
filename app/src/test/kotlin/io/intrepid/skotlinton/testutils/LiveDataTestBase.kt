package io.intrepid.skotlinton.testutils

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import org.junit.Rule

abstract class LiveDataTestBase {
    @Rule
    @JvmField
    val instantTaskExecutorRule = InstantTaskExecutorRule()
}
