package io.intrepid.skotlinton.screens.example1

import com.nhaarman.mockitokotlin2.whenever
import io.intrepid.skotlinton.testutils.ViewModelTestBase
import io.intrepid.skotlinton.utils.ViewEvent
import io.reactivex.observers.TestObserver
import org.junit.Before
import org.junit.Test

internal class Example1ViewModelTest : ViewModelTestBase() {

    private lateinit var viewModel: Example1ViewModel
    private lateinit var eventObserver: TestObserver<ViewEvent>

    @Before
    fun setup() {
        viewModel = Example1ViewModel(testConfiguration)
        eventObserver = viewModel.eventObservable.test()
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
