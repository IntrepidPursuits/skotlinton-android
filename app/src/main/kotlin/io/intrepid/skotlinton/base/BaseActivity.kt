package io.intrepid.skotlinton.base

import android.content.Intent
import android.os.Bundle
import androidx.annotation.CallSuper
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LifecycleOwner
import butterknife.ButterKnife
import io.intrepid.skotlinton.SkotlintonApplication
import io.intrepid.skotlinton.utils.LiveDataObserver
import timber.log.Timber

abstract class BaseActivity : AppCompatActivity(), LiveDataObserver {

    override val liveDataLifecycleOwner: LifecycleOwner get() = this

    protected abstract val layoutResourceId: Int
    protected val skotlintonApplication: SkotlintonApplication
        get() = application as SkotlintonApplication

    override fun onCreate(savedInstanceState: Bundle?) {
        Timber.v("Lifecycle onCreate: $this")
        super.onCreate(savedInstanceState)

        setContentView(layoutResourceId)
        ButterKnife.bind(this)
    }

    @CallSuper
    override fun onNewIntent(intent: Intent) {
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
}
