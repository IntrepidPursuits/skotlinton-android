package io.intrepid.skotlinton.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.intrepid.skotlinton.utils.ViewEvent
import io.intrepid.skotlinton.utils.applySchedulers
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.PublishSubject

open class BaseViewModel(configuration: ViewModelConfiguration) : ViewModel() {

    protected val ioScheduler = configuration.ioScheduler
    protected val uiScheduler = configuration.uiScheduler
    protected val userSettings = configuration.userSettings
    protected val restApi = configuration.restApi
    protected val timeProvider = configuration.timeProvider
    protected val crashReporter = configuration.crashReporter

    protected val disposables = CompositeDisposable()

    private val eventPublisher: PublishSubject<ViewEvent> = PublishSubject.create()
    val eventObservable: Observable<ViewEvent> = eventPublisher

    // Works exactly the same way as MutableLiveData.value
    // This allows all the subclasses' live data to be declared as LiveData<T> type instead of MutableLiveData<T> so
    // that their values can't be changed externally but still can internally
    // see https://github.com/IntrepidPursuits/skotlinton-android/pull/33#discussion_r275908063
    protected var <T : Any> LiveData<T>.latestValue: T?
        get() = this.value
        set(value) {
            (this as MutableLiveData<T>).value = value
        }

    protected fun sendViewEvent(viewEvent: ViewEvent) = eventPublisher.onNext(viewEvent)

    protected fun <T> Observable<T>.subscribeOnIoObserveOnUi(): Observable<T> = applySchedulers(ioScheduler, uiScheduler)

    protected fun <T> Single<T>.subscribeOnIoObserveOnUi(): Single<T> = applySchedulers(ioScheduler, uiScheduler)

    override fun onCleared() {
        super.onCleared()
        disposables.clear()
    }
}
