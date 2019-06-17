package io.intrepid.skotlinton.base

import io.intrepid.skotlinton.logging.CrashReporter
import io.intrepid.skotlinton.rest.RestApi
import io.intrepid.skotlinton.settings.UserSettings
import io.intrepid.skotlinton.utils.TimeProvider
import io.reactivex.Scheduler
import kotlinx.coroutines.CoroutineScope

/**
 * Wrapper class for common dependencies that all view models are expected to have
 */
open class ViewModelConfiguration(
    open val ioScheduler: Scheduler,
    open val uiScheduler: Scheduler,
    val userSettings: UserSettings,
    val restApi: RestApi,
    val timeProvider: TimeProvider,
    val crashReporter: CrashReporter,
    open val coroutineScope: CoroutineScope
)
