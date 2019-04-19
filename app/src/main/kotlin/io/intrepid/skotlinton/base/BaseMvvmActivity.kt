package io.intrepid.skotlinton.base

import android.os.Bundle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import io.intrepid.skotlinton.di.ActivityComponent
import io.intrepid.skotlinton.di.ActivityModule
import io.intrepid.skotlinton.utils.ViewEvent
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.rxkotlin.subscribeBy
import javax.inject.Inject

/**
 * Base class for activities that will have some business logic instead of just hosting a fragment.
 * If the activity is only going to act as a container for a fragment, use {@link BaseFragmentActivity}
 * instead
 */
abstract class BaseMvvmActivity<VM : BaseViewModel> : BaseActivity() {

    @Suppress("UNCHECKED_CAST")
    protected val viewModel: VM by lazy(LazyThreadSafetyMode.NONE) {
        val factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return createViewModel(commonViewModelDependencies) as T
            }
        }
        ViewModelProviders.of(this, factory).get(viewModelClass) as VM
    }

    private val viewEventDisposables = CompositeDisposable()

    // All subclass should just override this and call `component.inject(this)`. Due to Dagger limitations, this can't
    // be done in the base class
    abstract fun injectDagger(component: ActivityComponent)
    abstract val viewModelClass: Class<out ViewModel>
    abstract fun createViewModel(commonDependencies: CommonViewModelDependencies): VM

    @Inject
    lateinit var commonViewModelDependencies: CommonViewModelDependencies

    /**
     * Override [onViewCreated] to handle any logic that needs to occur right after inflating the view.
     * onViewCreated is called immediately after onCreateView
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val activityComponent = skotlintonApplication.component.activityComponent(ActivityModule())
        injectDagger(activityComponent)
        onViewCreated(savedInstanceState)
    }

    /**
     * Override this method to do any additional view initialization (ex: setup RecycleView adapter)
     */
    protected open fun onViewCreated(savedInstanceState: Bundle?) {

    }

    override fun onStart() {
        super.onStart()
        viewEventDisposables += viewModel.eventObservable
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(onNext = {
                    onViewEvent(it)
                })
    }

    override fun onStop() {
        super.onStop()
        viewEventDisposables.clear()
    }

    protected open fun onViewEvent(event: ViewEvent) {
    }
}
