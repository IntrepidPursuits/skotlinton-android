package io.intrepid.skotlinton.base

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.annotation.CallSuper
import android.support.v7.app.AppCompatActivity
import butterknife.ButterKnife
import io.intrepid.skotlinton.SkotlintonApplication
import timber.log.Timber
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper

@Suppress("AddVarianceModifier")
abstract class BaseActivity<S : BaseScreen, P : BasePresenter<S>> : AppCompatActivity() {

    protected abstract val layoutResourceId: Int
    protected val skotlintonApplication: SkotlintonApplication
        get() = application as SkotlintonApplication

    protected lateinit var presenter: P

    override fun onCreate(savedInstanceState: Bundle?) {
        Timber.v("Lifecycle onCreate: $this")
        super.onCreate(savedInstanceState)

        setContentView(layoutResourceId)
        ButterKnife.bind(this)
        val configuration = skotlintonApplication.getPresenterConfiguration()
        presenter = createPresenter(configuration).also { lifecycle.addObserver(it) }
        onViewCreated(savedInstanceState)
    }

    @CallSuper
    override fun onNewIntent(intent: Intent?) {
        Timber.v("Lifecycle onNewIntent: $this")
        super.onNewIntent(intent)
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
    override fun onDestroy() {
        Timber.v("Lifecycle onDestroy: $this")
        super.onDestroy()
    }

    @CallSuper
    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase))
    }

    abstract fun createPresenter(configuration: PresenterConfiguration): P

    /**
     * Override this method to do any additional screen initialization (ex: setup RecycleView adapter)
     */
    protected open fun onViewCreated(savedInstanceState: Bundle?) {

    }
}
