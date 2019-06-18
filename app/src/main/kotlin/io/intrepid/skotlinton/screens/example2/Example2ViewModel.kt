package io.intrepid.skotlinton.screens.example2

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.intrepid.skotlinton.base.BaseViewModel
import io.intrepid.skotlinton.base.ViewModelConfiguration
import kotlinx.coroutines.launch
import timber.log.Timber

class Example2ViewModel(configuration: ViewModelConfiguration) : BaseViewModel(configuration) {

    val currentIpAddressText: LiveData<String> = MutableLiveData()
    val previousIpAddressVisible: LiveData<Boolean> = MutableLiveData()
    val previousIpAddressText: LiveData<String> = MutableLiveData()

    init {
        currentIpAddressText.latestValue = "Retrieving your current IP address"
        coroutineScope.launch {
            try {
                val ip = restApi.getMyIp().ip
                currentIpAddressText.latestValue = "Your current IP address is $ip"
                userSettings.lastIp = ip
            } catch (e: Exception) {
                Timber.w(e, "Can't retrieve ip")
            }
        }

        val lastIp = userSettings.lastIp
        if (lastIp.isNullOrEmpty()) {
            previousIpAddressVisible.latestValue = false
        } else {
            previousIpAddressVisible.latestValue = true
            previousIpAddressText.latestValue = "Your previous IP address is $lastIp"
        }
    }
}
