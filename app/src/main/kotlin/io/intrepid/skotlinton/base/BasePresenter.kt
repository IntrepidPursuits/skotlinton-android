package io.intrepid.skotlinton.base

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.OnLifecycleEvent
import io.intrepid.skotlinton.logging.CrashReporter
import io.intrepid.skotlinton.rest.RestApi
import io.intrepid.skotlinton.settings.UserSettings
import io.intrepid.skotlinton.utils.applySchedulers
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import timber.log.Timber

abstract class BasePresenter<S : BaseScreen>(protected var screen: S, configuration: PresenterConfiguration) : LifecycleObserver {

    private val ioScheduler: Scheduler = configuration.ioScheduler
    private val uiScheduler: Scheduler = configuration.uiScheduler
    protected val userSettings: UserSettings = configuration.userSettings
    protected val restApi: RestApi = configuration.restApi
    protected val crashReporter: CrashReporter = configuration.crashReporter

    protected val disposables: CompositeDisposable = CompositeDisposable()

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun onCreateScreenLifecycle() {
        Timber.v("Presenter onCreateScreenLifecycle: $this")
        screenCreated()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun onStopScreenLifecycle() {
        disposables.clear()
        Timber.v("Presenter onStopScreenLifecycle: $this")
    }

    open fun screenCreated() {}

    fun <T> Observable<T>.subscribeOnIoObserveOnUi(): Observable<T> {
        return applySchedulers(ioScheduler, uiScheduler)
    }

    fun <T> Single<T>.subscribeOnIoObserveOnUi(): Single<T> {
        return applySchedulers(ioScheduler, uiScheduler)
    }
}
