package io.intrepid.skeleton.base

import io.intrepid.skotlinton.base.BaseContract
import io.intrepid.skotlinton.base.BasePresenter
import io.intrepid.skotlinton.base.PresenterConfiguration
import io.intrepid.skotlinton.testutils.PresenterTestBase
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.*

class BasePresenterTest : PresenterTestBase<BasePresenter<BaseContract.View>>() {
    @Mock
    private lateinit var view: BaseContract.View

    @Mock
    private lateinit var onBind: Runnable

    @Mock
    private lateinit var onUnbind: Runnable

    private lateinit var disposable: Disposable

    @Before
    fun setUp() {
        presenter = TestPresenter(view, testConfiguration)
    }

    @Test
    fun bind_onlyOnce() {
        presenter.bindView(view)
        presenter.bindView(view)
        verify<Runnable>(onBind, times(1)).run()
    }

    @Test
    fun unbind_onlyOnce() {
        presenter.unbindView()
        verify<Runnable>(onUnbind, never()).run()

        presenter.bindView(view)
        presenter.unbindView()
        presenter.unbindView()
        verify<Runnable>(onUnbind, times(1)).run()
    }

    @Test
    fun unbind_clearsDisposables() {
        presenter.bindView(view)
        assertFalse(disposable.isDisposed)

        presenter.unbindView()
        assertTrue(disposable.isDisposed)
    }

    private inner class TestPresenter(view: BaseContract.View, configuration: PresenterConfiguration) :
        BasePresenter<BaseContract.View>(view, configuration) {

        override fun onViewBound() {
            onBind.run()
            disposable = Observable.never<Any>().subscribe()
            disposables.add(disposable)
        }

        override fun onViewUnbound() {
            onUnbind.run()
        }
    }

}
