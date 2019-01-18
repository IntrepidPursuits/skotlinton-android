package io.intrepid.skotlinton.screens.example2

import com.jakewharton.rxrelay2.BehaviorRelay
import io.intrepid.skotlinton.base.BaseViewModel
import io.intrepid.skotlinton.base.ViewModelConfiguration
import io.intrepid.skotlinton.utils.RxUtils
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.rxkotlin.subscribeBy

class Example2ViewModel(configuration: ViewModelConfiguration) : BaseViewModel(configuration) {

    val currentIpAddressText = BehaviorRelay.create<String>()
    val previousIpAddressVisible = BehaviorRelay.create<Boolean>()
    val previousIpAddressText = BehaviorRelay.create<String>()

    init {
        currentIpAddressText.accept("Retrieving your current IP address")
        networkDisposables += restApi.getMyIp()
                .subscribeOnIoObserveOnUi()
                .subscribeBy(
                        onSuccess = {
                            val ip = it.ip
                            currentIpAddressText.accept("Your current IP address is $ip")
                            userSettings.lastIp = ip
                        },
                        onError = RxUtils.logError()
                )

        val lastIp = userSettings.lastIp
        if (lastIp.isNullOrEmpty()) {
            previousIpAddressVisible.accept(false)
        } else {
            previousIpAddressVisible.accept(true)
            previousIpAddressText.accept("Your previous IP address is $lastIp")
        }
    }
}
