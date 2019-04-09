package io.intrepid.skotlinton.screens.example1

import io.intrepid.skotlinton.base.BaseViewModel
import io.intrepid.skotlinton.base.ViewModelConfiguration
import io.intrepid.skotlinton.screens.example1.Example1ViewEvent.GotoExample2
import io.intrepid.skotlinton.screens.example1.Example1ViewEvent.ShowCurrentTimeToast

class Example1ViewModel(configuration: ViewModelConfiguration) : BaseViewModel(configuration) {

    fun onNextClick() {
        sendViewEvent(GotoExample2)
    }

    fun onShowToastClick() {
        sendViewEvent(ShowCurrentTimeToast("Current unix time is ${timeProvider.currentTimeMillis}"))
    }
}
