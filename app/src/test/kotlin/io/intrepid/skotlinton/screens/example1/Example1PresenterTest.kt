package io.intrepid.skotlinton.screens.example1

import io.intrepid.skotlinton.testutils.PresenterTestBase
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.verify

internal class Example1PresenterTest : PresenterTestBase<Example1Presenter>() {

    @Mock
    lateinit var mockView: Example1Contract.View

    @Before
    fun setup() {
        presenter = Example1Presenter(mockView, testConfiguration)
    }

    @Test
    fun onButtonClick() {
        presenter.onButtonClick()
        verify(mockView).gotoExample2()
    }
}
