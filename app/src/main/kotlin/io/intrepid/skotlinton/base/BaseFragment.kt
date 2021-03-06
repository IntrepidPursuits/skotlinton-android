package io.intrepid.skotlinton.base

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.CallSuper
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import butterknife.ButterKnife
import butterknife.Unbinder
import io.intrepid.skotlinton.SkotlintonApplication
import io.intrepid.skotlinton.utils.LiveDataObserver
import io.intrepid.skotlinton.utils.ViewEvent
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.rxkotlin.subscribeBy
import timber.log.Timber

abstract class BaseFragment<out VM : BaseViewModel> : Fragment(), LiveDataObserver {

    override val liveDataLifecycleOwner: LifecycleOwner get() = viewLifecycleOwner

    protected val skotlintonApplication: SkotlintonApplication
        get() = requireActivity().application as SkotlintonApplication
    protected abstract val layoutResourceId: Int

    @Suppress("UNCHECKED_CAST")
    protected val viewModel: VM by lazy(LazyThreadSafetyMode.NONE) {
        val factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                val configuration = skotlintonApplication.getViewModelConfiguration()
                return createViewModel(configuration) as T
            }
        }
        ViewModelProviders.of(this, factory).get(viewModelClass) as VM
    }

    private val viewEventDisposables = CompositeDisposable()

    abstract val viewModelClass: Class<out ViewModel>
    abstract fun createViewModel(configuration: ViewModelConfiguration): VM

    private var unbinder: Unbinder? = null

    @CallSuper
    override fun onAttach(context: Context) {
        Timber.v("Lifecycle onAttach: $this to $context")
        super.onAttach(context)
    }

    @CallSuper
    override fun onCreate(savedInstanceState: Bundle?) {
        Timber.v("Lifecycle onCreate: $this")
        super.onCreate(savedInstanceState)
    }

    /**
     * Override [onViewCreated] to handle any logic that needs to occur right after inflating the view.
     * onViewCreated is called immediately after onCreateView
     */
    final override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Timber.v("Lifecycle onCreateView: $this")
        val view = inflater.inflate(layoutResourceId, container, false)
        unbinder = ButterKnife.bind(this, view)
        return view
    }

    final override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onViewCreated(savedInstanceState)
    }

    /**
     * Override this method to do any additional view initialization (ex: setup RecyclerView adapter)
     */
    @CallSuper
    protected open fun onViewCreated(savedInstanceState: Bundle?) {
    }

    @CallSuper
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        Timber.v("Lifecycle onActivityResult: $this")
        super.onActivityResult(requestCode, resultCode, data)
    }

    @CallSuper
    override fun onStart() {
        Timber.v("Lifecycle onStart: $this")
        super.onStart()
        viewEventDisposables += viewModel.eventObservable
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(onNext = {
                    onViewEvent(it)
                })
    }

    @CallSuper
    override fun onResume() {
        Timber.v("Lifecycle onResume: $this")
        super.onResume()
    }

    @CallSuper
    override fun onPause() {
        Timber.v("Lifecycle onPause: $this")
        super.onPause()
    }

    @CallSuper
    override fun onStop() {
        Timber.v("Lifecycle onStop: $this")
        super.onStop()
        viewEventDisposables.clear()
    }

    @CallSuper
    override fun onDestroyView() {
        Timber.v("Lifecycle onDestroyView: $this")
        super.onDestroyView()
        unbinder?.unbind()
    }

    @CallSuper
    override fun onDestroy() {
        Timber.v("Lifecycle onDestroy: $this")
        super.onDestroy()
    }

    @CallSuper
    override fun onDetach() {
        Timber.v("Lifecycle onDetach: $this from $context")
        super.onDetach()
    }

    protected open fun onViewEvent(event: ViewEvent) {
    }
}
