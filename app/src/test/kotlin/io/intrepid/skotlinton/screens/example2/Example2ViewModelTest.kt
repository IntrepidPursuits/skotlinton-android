package io.intrepid.skotlinton.screens.example2

import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import io.intrepid.skotlinton.models.IpModel
import io.intrepid.skotlinton.testutils.BaseViewModelTest
import io.reactivex.Single
import org.amshove.kluent.shouldEqual
import org.junit.Test

internal class Example2ViewModelTest : BaseViewModelTest() {

    private lateinit var viewModel: Example2ViewModel

    @Test
    fun init_hasPreviousIp() {
        val mockIp = "127.0.0.1"
        val mockPreviousIp = "127.0.0.2"

        val mockIpModel = IpModel()
        mockIpModel.ip = mockIp
        whenever(mockRestApi.getMyIp()).thenReturn(Single.just(mockIpModel))
        whenever(mockUserSettings.lastIp).thenReturn(mockPreviousIp)

        viewModel = Example2ViewModel(testConfiguration)

        viewModel.currentIpAddressText.value shouldEqual "Retrieving your current IP address"
        viewModel.previousIpAddressText.value shouldEqual "Your previous IP address is 127.0.0.2"
        viewModel.previousIpAddressVisible.value shouldEqual true
        testConfiguration.triggerRxSchedulers()
        viewModel.currentIpAddressText.value shouldEqual "Your current IP address is 127.0.0.1"
        verify(mockUserSettings).lastIp = mockIp
    }

    @Test
    @Throws(Exception::class)
    fun init_NoPreviousIp() {
        whenever(mockRestApi.getMyIp()).thenReturn(Single.error(Throwable()))
        whenever(mockUserSettings.lastIp).thenReturn("")

        viewModel = Example2ViewModel(testConfiguration)
        viewModel.previousIpAddressVisible.value shouldEqual false
    }
}
