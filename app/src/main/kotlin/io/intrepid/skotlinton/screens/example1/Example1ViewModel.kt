package io.intrepid.skotlinton.screens.example1

import com.jakewharton.rxrelay2.PublishRelay
import io.intrepid.skotlinton.base.BaseViewModel
import io.intrepid.skotlinton.base.ViewModelConfiguration
import io.intrepid.skotlinton.screens.example1.Example1ViewEvent.GotoExample2
import io.intrepid.skotlinton.screens.example1.Example1ViewEvent.ShowCurrentTimeToast

class Example1ViewModel(configuration: ViewModelConfiguration) : BaseViewModel(configuration) {

    val eventPublisher: PublishRelay<Example1ViewEvent> = PublishRelay.create()

    fun onNextClick() {
        eventPublisher.accept(GotoExample2)
    }

    fun onShowToastClick() {
        eventPublisher.accept(ShowCurrentTimeToast("Current unix time is ${timeProvider.currentTimeMillis}"))
    }
}
