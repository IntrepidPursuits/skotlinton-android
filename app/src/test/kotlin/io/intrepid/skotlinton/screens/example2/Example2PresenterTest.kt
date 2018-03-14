package io.intrepid.skotlinton.screens.example2

import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import io.intrepid.skotlinton.models.IpModel
import io.intrepid.skotlinton.testutils.PresenterTestBase
import io.reactivex.Single
import org.junit.Before
import org.junit.Test
import org.mockito.Mock

internal class Example2PresenterTest : PresenterTestBase<Example2Presenter>() {
    @Mock
    private lateinit var mockView: Example2Contract.View

    @Before
    fun setup() {
        presenter = Example2Presenter(mockView, testConfiguration)
    }

    @Test
    fun onViewCreated() {
        val mockIp = "127.0.0.1"
        val mockPreviousIp = "127.0.0.2"

        val mockIpModel = IpModel()
        mockIpModel.ip = mockIp
        whenever(mockRestApi.getMyIp()).thenReturn(Single.just(mockIpModel))
        whenever(mockUserSettings.lastIp).thenReturn(mockPreviousIp)

        presenter.onViewCreated()
        verify(mockView).showPreviousIpAddress(mockPreviousIp)
        testConfiguration.triggerRxSchedulers()
        verify(mockView).showCurrentIpAddress(mockIp)
        verify(mockUserSettings).lastIp = mockIp
    }

    @Test
    @Throws(Exception::class)
    fun onViewCreated_NoPreviousIp() {
        whenever(mockRestApi.getMyIp()).thenReturn(Single.error(Throwable()))
        whenever(mockUserSettings.lastIp).thenReturn("")

        presenter.onViewCreated()
        verify(mockView).hidePreviousIpAddress()
    }
}
