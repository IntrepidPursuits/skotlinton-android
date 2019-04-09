package io.intrepid.skotlinton.base

import androidx.lifecycle.ViewModel
import com.jakewharton.rxrelay2.PublishRelay
import io.intrepid.skotlinton.utils.ViewEvent
import io.intrepid.skotlinton.utils.applySchedulers
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable

open class BaseViewModel(configuration: ViewModelConfiguration) : ViewModel() {

    protected val ioScheduler = configuration.ioScheduler
    protected val uiScheduler = configuration.uiScheduler
    protected val userSettings = configuration.userSettings
    protected val restApi = configuration.restApi
    protected val timeProvider = configuration.timeProvider
    protected val crashReporter = configuration.crashReporter

    protected val disposables = CompositeDisposable()

    val eventPublisher: PublishRelay<ViewEvent> = PublishRelay.create()

    protected fun sendViewEvent(viewEvent: ViewEvent) = eventPublisher.accept(viewEvent)

    protected fun <T> Observable<T>.subscribeOnIoObserveOnUi(): Observable<T> = applySchedulers(ioScheduler, uiScheduler)

    protected fun <T> Single<T>.subscribeOnIoObserveOnUi(): Single<T> = applySchedulers(ioScheduler, uiScheduler)

    override fun onCleared() {
        super.onCleared()
        disposables.clear()
    }
}
