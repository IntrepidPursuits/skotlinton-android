package io.intrepid.skotlinton.screens.example1

import io.intrepid.skotlinton.base.BasePresenter
import io.intrepid.skotlinton.base.PresenterConfiguration

class Example1Presenter(view: Example1Screen, configuration: PresenterConfiguration)
    : BasePresenter<Example1Screen>(view, configuration) {

    fun onButtonClick() {
        screen?.gotoExample2()
    }
}
