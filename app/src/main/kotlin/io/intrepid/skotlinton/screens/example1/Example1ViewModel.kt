package io.intrepid.skotlinton.screens.example1

import io.intrepid.skotlinton.base.BaseViewModel
import io.intrepid.skotlinton.base.CommonViewModelDependencies
import io.intrepid.skotlinton.screens.example1.Example1ViewEvent.GotoExample2
import io.intrepid.skotlinton.screens.example1.Example1ViewEvent.ShowCurrentTimeToast
import io.intrepid.skotlinton.utils.TimeProvider

class Example1ViewModel(
        commonDependencies: CommonViewModelDependencies,
        private val timeProvider: TimeProvider
) : BaseViewModel(commonDependencies) {

    fun onNextClick() {
        sendViewEvent(GotoExample2)
    }

    fun onShowToastClick() {
        sendViewEvent(ShowCurrentTimeToast("Current unix time is ${timeProvider.currentTimeMillis}"))
    }
}
