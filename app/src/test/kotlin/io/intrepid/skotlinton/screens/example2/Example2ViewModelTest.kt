package io.intrepid.skotlinton.screens.example2

import io.intrepid.skotlinton.models.IpModel
import io.intrepid.skotlinton.testutils.ViewModelTestBase
import io.mockk.coEvery
import io.mockk.every
import io.mockk.verify
import kotlinx.coroutines.delay
import org.amshove.kluent.shouldEqual
import org.junit.Test

internal class Example2ViewModelTest : ViewModelTestBase() {

    private val simulatedDelay: Long = 1000

    private lateinit var viewModel: Example2ViewModel

    @Test
    fun init_hasPreviousIp() {
        val mockIp = "127.0.0.1"
        val mockPreviousIp = "127.0.0.2"

        val mockIpModel = IpModel()
        mockIpModel.ip = mockIp

        coEvery {
            mockRestApi.getMyIp()
        } coAnswers {
            delay(simulatedDelay)
            mockIpModel
        }
        every { mockUserSettings.lastIp } returns mockPreviousIp

        viewModel = Example2ViewModel(testConfiguration)

        viewModel.currentIpAddressText.value shouldEqual "Retrieving your current IP address"
        viewModel.previousIpAddressText.value shouldEqual "Your previous IP address is 127.0.0.2"
        viewModel.previousIpAddressVisible.value shouldEqual true

        coroutineScope.advanceTimeBy(simulatedDelay)

        viewModel.currentIpAddressText.value shouldEqual "Your current IP address is 127.0.0.1"

        verify { mockUserSettings.lastIp = mockIp }
    }

    @Test
    fun init_NoPreviousIp() {
        coEvery { mockRestApi.getMyIp() } throws RuntimeException()
        every { mockUserSettings.lastIp } returns ""

        viewModel = Example2ViewModel(testConfiguration)
        viewModel.previousIpAddressVisible.value shouldEqual false
    }
}
