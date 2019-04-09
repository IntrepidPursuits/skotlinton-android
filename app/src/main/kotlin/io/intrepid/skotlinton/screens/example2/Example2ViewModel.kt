package io.intrepid.skotlinton.screens.example2

import androidx.lifecycle.MutableLiveData
import io.intrepid.skotlinton.base.BaseViewModel
import io.intrepid.skotlinton.base.ViewModelConfiguration
import io.intrepid.skotlinton.utils.RxUtils
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.rxkotlin.subscribeBy

class Example2ViewModel(configuration: ViewModelConfiguration) : BaseViewModel(configuration) {

    val currentIpAddressText = MutableLiveData<String>()
    val previousIpAddressVisible = MutableLiveData<Boolean>()
    val previousIpAddressText = MutableLiveData<String>()

    init {
        currentIpAddressText.value = "Retrieving your current IP address"
        disposables += restApi.getMyIp()
                .subscribeOnIoObserveOnUi()
                .subscribeBy(
                        onSuccess = {
                            val ip = it.ip
                            currentIpAddressText.value = "Your current IP address is $ip"
                            userSettings.lastIp = ip
                        },
                        onError = RxUtils.logError()
                )

        val lastIp = userSettings.lastIp
        if (lastIp.isNullOrEmpty()) {
            previousIpAddressVisible.value = false
        } else {
            previousIpAddressVisible.value = true
            previousIpAddressText.value = "Your previous IP address is $lastIp"
        }
    }
}
