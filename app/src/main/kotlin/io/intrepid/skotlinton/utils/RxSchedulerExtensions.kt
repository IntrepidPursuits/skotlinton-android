package io.intrepid.skotlinton.utils

import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.Single

fun <T> Observable<T>.applySchedulers(subscribeOnScheduler: Scheduler, observeOnScheduler: Scheduler): Observable<T> {
    return this.subscribeOn(subscribeOnScheduler).observeOn(observeOnScheduler)
}

fun <T> Flowable<T>.applySchedulers(subscribeOnScheduler: Scheduler, observeOnScheduler: Scheduler): Flowable<T> {
    return this.subscribeOn(subscribeOnScheduler).observeOn(observeOnScheduler)
}

fun <T> Single<T>.applySchedulers(subscribeOnScheduler: Scheduler, observeOnScheduler: Scheduler): Single<T> {
    return this.subscribeOn(subscribeOnScheduler).observeOn(observeOnScheduler)
}

fun Completable.applySchedulers(subscribeOnScheduler: Scheduler, observeOnScheduler: Scheduler): Completable {
    return this.subscribeOn(subscribeOnScheduler).observeOn(observeOnScheduler)
}

fun <T> Maybe<T>.applySchedulers(subscribeOnScheduler: Scheduler, observeOnScheduler: Scheduler): Maybe<T> {
    return this.subscribeOn(subscribeOnScheduler).observeOn(observeOnScheduler)
}
