package io.intrepid.skotlinton.screens.example2

import io.intrepid.skotlinton.base.BaseScreen

interface Example2Screen : BaseScreen {

    fun showCurrentIpAddress(ip: String)

    fun showPreviousIpAddress(ip: String)

    fun hidePreviousIpAddress()
}
