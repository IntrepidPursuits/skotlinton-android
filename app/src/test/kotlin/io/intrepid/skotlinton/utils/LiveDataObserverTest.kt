package io.intrepid.skotlinton.utils

import android.view.View
import android.widget.TextView
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import androidx.lifecycle.MutableLiveData
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.amshove.kluent.shouldEqual
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class LiveDataObserverTest {
    @Rule
    @JvmField
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val lifecycleOwner: LifecycleOwner = mockk {
        every { lifecycle } answers { lifecycleRegistry }
    }
    private val mockObserver = MockObserver(lifecycleOwner)
    private val lifecycleRegistry = LifecycleRegistry(lifecycleOwner)

    @Before
    fun setup() {
        lifecycleRegistry.markState(Lifecycle.State.STARTED)
    }

    @Test
    fun observe() {
        var observedValue: String? = null

        val liveData = MutableLiveData<String>()
        with(mockObserver) {
            liveData.observe {
                observedValue = it
            }
        }
        liveData.value = "hello"

        observedValue shouldEqual "hello"
    }

    @Test
    fun bindToVisibility_Visible() {
        val view = mockk<View>(relaxed = true)

        val liveData = MutableLiveData<Boolean>()
        with(mockObserver) {
            liveData.bindToVisibility(view)
        }
        liveData.value = true

        verify { view.visibility = View.VISIBLE }
    }

    @Test
    fun bindToVisibility_Gone() {
        val view = mockk<View>(relaxed = true)

        val liveData = MutableLiveData<Boolean>()
        with(mockObserver) {
            liveData.bindToVisibility(view)
        }
        liveData.value = false

        verify { view.visibility = View.GONE }
    }

    @Test
    fun bindToText() {
        val view = mockk<TextView>(relaxed = true)

        val liveData = MutableLiveData<String>()
        with(mockObserver) {
            liveData.bindToText(view)
        }
        liveData.value = "hello"

        verify { view.text = "hello" }
    }
}

class MockObserver(private val lifecycleOwner: LifecycleOwner) : LiveDataObserver {
    override val liveDataLifecycleOwner: LifecycleOwner get() = lifecycleOwner
}
