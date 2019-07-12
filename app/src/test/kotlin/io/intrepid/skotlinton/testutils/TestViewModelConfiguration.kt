package io.intrepid.skotlinton.testutils

import io.intrepid.skotlinton.base.ViewModelConfiguration
import io.intrepid.skotlinton.logging.CrashReporter
import io.intrepid.skotlinton.rest.RestApi
import io.intrepid.skotlinton.settings.UserSettings
import io.intrepid.skotlinton.utils.TimeProvider
import io.mockk.mockk
import io.reactivex.schedulers.TestScheduler
import kotlinx.coroutines.test.TestCoroutineScope

class TestViewModelConfiguration private constructor(
    userSettings: UserSettings,
    restApi: RestApi,
    timeProvider: TimeProvider,
    crashReporter: CrashReporter,
    coroutineScope: TestCoroutineScope
) : ViewModelConfiguration(
    TestScheduler(),
    TestScheduler(),
    userSettings,
    restApi,
    timeProvider,
    crashReporter,
    coroutineScope
) {

    override val ioScheduler: TestScheduler get() = super.ioScheduler as TestScheduler
    override val uiScheduler: TestScheduler get() = super.uiScheduler as TestScheduler
    override val coroutineScope: TestCoroutineScope get() = super.coroutineScope as TestCoroutineScope

    companion object {
        fun createTestConfiguration(): TestViewModelConfiguration {
            val userSettings = mockk<UserSettings>()
            val restApi = mockk<RestApi>()
            val timeProvider = mockk<TimeProvider>()
            val crashReporter = mockk<CrashReporter>()
            val testCoroutineScope = TestCoroutineScope()
            return TestViewModelConfiguration(userSettings, restApi, timeProvider, crashReporter, testCoroutineScope)
        }
    }
}
