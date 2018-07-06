package io.intrepid.skotlinton.testutils

import io.intrepid.skotlinton.base.ViewModelConfiguration
import io.intrepid.skotlinton.logging.CrashReporter
import io.intrepid.skotlinton.rest.RestApi
import io.intrepid.skotlinton.settings.UserSettings
import io.intrepid.skotlinton.utils.TimeProvider
import io.reactivex.schedulers.TestScheduler
import org.mockito.Mockito

class TestViewModelConfiguration private constructor(userSettings: UserSettings, restApi: RestApi, timeProvider: TimeProvider, crashReporter: CrashReporter)
    : ViewModelConfiguration(TestScheduler(), TestScheduler(), userSettings, restApi, timeProvider, crashReporter) {

    override val ioScheduler: TestScheduler get() = super.ioScheduler as TestScheduler
    override val uiScheduler: TestScheduler get() = super.uiScheduler as TestScheduler

    companion object {
        fun createTestConfiguration(): TestViewModelConfiguration {
            val userSettings = Mockito.mock(UserSettings::class.java)
            val restApi = Mockito.mock(RestApi::class.java)
            val timeProvider = Mockito.mock(TimeProvider::class.java)
            val crashReporter = Mockito.mock(CrashReporter::class.java)
            return TestViewModelConfiguration(userSettings, restApi, timeProvider, crashReporter)
        }
    }

    fun triggerRxSchedulers() {
        ioScheduler.triggerActions()
        uiScheduler.triggerActions()
    }
}
