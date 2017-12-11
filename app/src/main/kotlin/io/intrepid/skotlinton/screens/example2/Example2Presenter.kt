package io.intrepid.skotlinton.screens.example2

import io.intrepid.skotlinton.base.BasePresenter
import io.intrepid.skotlinton.base.PresenterConfiguration
import io.intrepid.skotlinton.utils.RxUtils
import io.reactivex.functions.Consumer

class Example2Presenter(view: Example2Screen, configuration: PresenterConfiguration)
    : BasePresenter<Example2Screen>(view, configuration) {

    override fun screenCreated() {
        disposables.add(restApi.getMyIp()
                .subscribeOnIoObserveOnUi()
                .subscribe(Consumer {
                    val ip = it.ip
                    screen.showCurrentIpAddress(ip)
                    userSettings.lastIp = ip
                }, RxUtils.logError()))

        val lastIp = userSettings.lastIp
        if (lastIp.isEmpty()) {
            screen.hidePreviousIpAddress()
        } else {
            screen.showPreviousIpAddress(lastIp)
        }
    }
}
