package io.intrepid.skotlinton.testutils

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import io.intrepid.skotlinton.logging.CrashReporter
import io.intrepid.skotlinton.rest.RestApi
import io.reactivex.schedulers.TestScheduler
import org.junit.Before
import org.junit.Rule
import org.mockito.junit.MockitoJUnit
import org.mockito.junit.MockitoRule

abstract class ViewModelTestBase {
    @Rule
    @JvmField
    val mockitoRule: MockitoRule = MockitoJUnit.rule()
    @Rule
    @JvmField
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    protected lateinit var commonDependencies: TestCommonViewModelDependencies
    protected lateinit var ioScheduler: TestScheduler
    protected lateinit var uiScheduler: TestScheduler
    protected lateinit var mockRestApi: RestApi
    protected lateinit var mockCrashReporter: CrashReporter

    @Before
    fun baseSetup() {
        commonDependencies = TestCommonViewModelDependencies.createTestDependencies()
        ioScheduler = commonDependencies.ioScheduler
        uiScheduler = commonDependencies.uiScheduler
        mockRestApi = commonDependencies.restApi
        mockCrashReporter = commonDependencies.crashReporter
    }
}
