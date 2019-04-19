package io.intrepid.skotlinton.base

import io.intrepid.skotlinton.logging.CrashReporter
import io.intrepid.skotlinton.rest.RestApi
import io.reactivex.Scheduler

/**
 * Wrapper class for common dependencies that all view models are expected to have.
 * Not every VM will use every one of these dependencies, but they are used often enough to be grouped here.
 * This is mainly to reduce the boilerplate of having to pass in these dependencies for every view model.
 */
open class CommonViewModelDependencies(
        open val ioScheduler: Scheduler,
        open val uiScheduler: Scheduler,
        val restApi: RestApi,
        val crashReporter: CrashReporter
)
