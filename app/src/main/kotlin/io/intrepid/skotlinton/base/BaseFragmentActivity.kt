package io.intrepid.skotlinton.base

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import io.intrepid.skotlinton.R

/**
 * Base class for activities whose sole purpose to to host a fragment. Child classes simply need
 * to override [.createFragment] and create the appropriate Fragment. If the activity
 * contains any additional logic, use [BaseActivity] instead
 */
abstract class BaseFragmentActivity : BaseActivity<BaseScreen, EmptyPresenter>(), BaseScreen {

    override val layoutResourceId: Int = R.layout.activity_fragment_container

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (savedInstanceState == null) {
            val fragment = createFragment(intent)
            supportFragmentManager.beginTransaction().add(R.id.fragment_container, fragment).commit()
        }
    }

    override fun createPresenter(configuration: PresenterConfiguration): EmptyPresenter {
        return EmptyPresenter(this, configuration)
    }

    protected abstract fun createFragment(intent: Intent?): Fragment
}
