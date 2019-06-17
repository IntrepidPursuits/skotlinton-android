package io.intrepid.skotlinton.testutils

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import io.intrepid.skotlinton.logging.CrashReporter
import io.intrepid.skotlinton.rest.RestApi
import io.intrepid.skotlinton.settings.UserSettings
import io.intrepid.skotlinton.utils.TimeProvider
import io.reactivex.schedulers.TestScheduler
import kotlinx.coroutines.test.TestCoroutineScope
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

    protected lateinit var testConfiguration: TestViewModelConfiguration
    protected lateinit var ioScheduler: TestScheduler
    protected lateinit var uiScheduler: TestScheduler
    protected lateinit var mockRestApi: RestApi
    protected lateinit var mockUserSettings: UserSettings
    protected lateinit var mockTimeProvider: TimeProvider
    protected lateinit var mockCrashReporter: CrashReporter
    protected lateinit var coroutineScope: TestCoroutineScope

    @Before
    fun baseSetup() {
        testConfiguration = TestViewModelConfiguration.createTestConfiguration()
        ioScheduler = testConfiguration.ioScheduler
        uiScheduler = testConfiguration.uiScheduler
        mockRestApi = testConfiguration.restApi
        mockUserSettings = testConfiguration.userSettings
        mockTimeProvider = testConfiguration.timeProvider
        mockCrashReporter = testConfiguration.crashReporter
        coroutineScope = testConfiguration.coroutineScope
    }
}
