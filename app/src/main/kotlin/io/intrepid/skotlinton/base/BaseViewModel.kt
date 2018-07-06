package io.intrepid.skotlinton.base

import android.arch.lifecycle.ViewModel
import io.intrepid.skotlinton.utils.applySchedulers
import io.reactivex.Observable
import io.reactivex.Single

open class BaseViewModel(configuration: ViewModelConfiguration) : ViewModel() {

    protected val ioScheduler = configuration.ioScheduler
    protected val uiScheduler = configuration.uiScheduler
    protected val userSettings = configuration.userSettings
    protected val restApi = configuration.restApi
    protected val timeProvider = configuration.timeProvider
    protected val crashReporter = configuration.crashReporter

    fun <T> Observable<T>.subscribeOnIoObserveOnUi(): Observable<T> = applySchedulers(ioScheduler, uiScheduler)

    fun <T> Single<T>.subscribeOnIoObserveOnUi(): Single<T> = applySchedulers(ioScheduler, uiScheduler)
}
