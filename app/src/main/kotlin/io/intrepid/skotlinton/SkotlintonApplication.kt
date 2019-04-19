package io.intrepid.skotlinton

import android.app.Application
import com.squareup.leakcanary.LeakCanary
import io.intrepid.skotlinton.di.*
import io.intrepid.skotlinton.logging.CrashlyticsReporter
import io.intrepid.skotlinton.logging.TimberConfig

open class SkotlintonApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        setupLeakCanary()

        CrashlyticsReporter.init(this)

        TimberConfig.init(CrashlyticsReporter)

        setupDagger()

    }

    lateinit var component: ApplicationComponent

    protected open fun setupDagger() {
        component = DaggerApplicationComponent.builder()
                .applicationModule(ApplicationModule(this))
                .networkingModule(NetworkingModule())
                .rxModule(RxModule())
                .build()
    }

    protected open fun setupLeakCanary() {
        LeakCanary.install(this)
    }
}
