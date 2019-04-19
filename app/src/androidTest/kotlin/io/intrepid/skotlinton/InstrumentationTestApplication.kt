package io.intrepid.skotlinton

import io.intrepid.skotlinton.di.DaggerTestApplicationComponent
import io.intrepid.skotlinton.di.TestApplicationModule
import io.intrepid.skotlinton.di.TestNetworkingModule
import io.intrepid.skotlinton.di.TestRxModule

class InstrumentationTestApplication : SkotlintonApplication() {

    override fun setupLeakCanary() {
        // noop, we don't want LeakCanary in UI tests
    }

    override fun setupDagger() {
        component = DaggerTestApplicationComponent.builder()
                .testApplicationModule(TestApplicationModule)
                .testNetworkingModule(TestNetworkingModule)
                .testRxModule(TestRxModule)
                .build()
    }
}
