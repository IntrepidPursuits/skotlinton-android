package io.intrepid.skotlinton.screens.example2

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.intrepid.skotlinton.base.BaseViewModel
import io.intrepid.skotlinton.base.ViewModelConfiguration
import io.intrepid.skotlinton.utils.RxUtils
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.rxkotlin.subscribeBy

class Example2ViewModel(configuration: ViewModelConfiguration) : BaseViewModel(configuration) {

    val currentIpAddressText: LiveData<String> = MutableLiveData()
    val previousIpAddressVisible: LiveData<Boolean> = MutableLiveData()
    val previousIpAddressText: LiveData<String> = MutableLiveData()

    init {
        currentIpAddressText.latestValue = "Retrieving your current IP address"
        disposables += restApi.getMyIp()
                .subscribeOnIoObserveOnUi()
                .subscribeBy(
                        onSuccess = {
                            val ip = it.ip
                            currentIpAddressText.latestValue = "Your current IP address is $ip"
                            userSettings.lastIp = ip
                        },
                        onError = RxUtils.logError()
                )

        val lastIp = userSettings.lastIp
        if (lastIp.isNullOrEmpty()) {
            previousIpAddressVisible.latestValue = false
        } else {
            previousIpAddressVisible.latestValue = true
            previousIpAddressText.latestValue = "Your previous IP address is $lastIp"
        }
    }
}
