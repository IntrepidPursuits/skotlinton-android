package io.intrepid.skotlinton.base

import android.content.Context
import android.os.Bundle
import android.support.annotation.CallSuper
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import butterknife.ButterKnife
import butterknife.Unbinder
import io.intrepid.skotlinton.SkotlintonApplication
import timber.log.Timber

@Suppress("AddVarianceModifier")
abstract class BaseFragment<S : BaseScreen, P : BasePresenter<S>> : Fragment() {

    protected val skotlintonApplication: SkotlintonApplication
        get() = activity.application as SkotlintonApplication
    protected abstract val layoutResourceId: Int

    protected lateinit var presenter: P
    private var unbinder: Unbinder? = null

    @CallSuper
    override fun onAttach(context: Context?) {
        Timber.v("Lifecycle onAttach: $this to $context")
        super.onAttach(context)
    }

    @CallSuper
    override fun onCreate(savedInstanceState: Bundle?) {
        Timber.v("Lifecycle onCreate: $this")
        super.onCreate(savedInstanceState)
    }

    /**
     * Override [.onViewCreated] to handle any logic that needs to occur right after inflating the screen.
     * onViewCreated is called immediately after onCreateView
     */
    override final fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        Timber.v("Lifecycle onCreateView: $this")
        val view = inflater.inflate(layoutResourceId, container, false)
        unbinder = ButterKnife.bind(this, view)
        return view
    }

    override final fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val configuration = skotlintonApplication.getPresenterConfiguration()
        presenter = createPresenter(configuration).also { lifecycle.addObserver(it) }
        onViewCreated(savedInstanceState)
    }

    /**
     * Override this method to do any additional screen initialization (ex: setup RecyclerView adapter)
     */
    protected open fun onViewCreated(savedInstanceState: Bundle?) {
    }

    abstract fun createPresenter(configuration: PresenterConfiguration): P

    @CallSuper
    override fun onStart() {
        Timber.v("Lifecycle onStart: " + this)
        super.onStart()
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

    override fun onDetach() {
        Timber.v("Lifecycle onDetach: $this from $context")
        super.onDetach()
    }
}
