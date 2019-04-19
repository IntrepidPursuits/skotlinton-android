package io.intrepid.skotlinton.testutils

import io.intrepid.skotlinton.base.CommonViewModelDependencies
import io.intrepid.skotlinton.logging.CrashReporter
import io.intrepid.skotlinton.rest.RestApi
import io.reactivex.schedulers.TestScheduler
import org.mockito.Mockito

class TestCommonViewModelDependencies private constructor(restApi: RestApi, crashReporter: CrashReporter)
    : CommonViewModelDependencies(TestScheduler(), TestScheduler(), restApi, crashReporter) {

    override val ioScheduler: TestScheduler get() = super.ioScheduler as TestScheduler
    override val uiScheduler: TestScheduler get() = super.uiScheduler as TestScheduler

    companion object {
        fun createTestDependencies(): TestCommonViewModelDependencies {
            val restApi = Mockito.mock(RestApi::class.java)
            val crashReporter = Mockito.mock(CrashReporter::class.java)
            return TestCommonViewModelDependencies(restApi, crashReporter)
        }
    }

    fun triggerRxSchedulers() {
        ioScheduler.triggerActions()
        uiScheduler.triggerActions()
    }
}
