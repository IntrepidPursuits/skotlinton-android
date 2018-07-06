package io.intrepid.skotlinton.screens.example1

import com.nhaarman.mockito_kotlin.whenever
import io.intrepid.skotlinton.testutils.BaseViewModelTest
import io.reactivex.observers.TestObserver
import org.junit.Before
import org.junit.Test

internal class Example1ViewModelTest : BaseViewModelTest() {

    private lateinit var viewModel: Example1ViewModel
    private lateinit var eventObserver: TestObserver<Example1ViewEvent>

    @Before
    fun setup() {
        viewModel = Example1ViewModel(testConfiguration)
        eventObserver = viewModel.eventPublisher.test()
    }

    @Test
    fun onButtonClick() {
        viewModel.onNextClick()
        eventObserver.assertValue { it == Example1ViewEvent.GotoExample2 }
    }

    @Test
    fun onShowToastClick() {
        whenever(mockTimeProvider.currentTimeMillis).thenReturn(1000)
        viewModel.onShowToastClick()
        eventObserver.assertValue { it is Example1ViewEvent.ShowCurrentTimeToast && it.message == "Current unix time is 1000" }
    }
}
