package io.intrepid.skotlinton.di

import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [
    TestApplicationModule::class,
    TestNetworkingModule::class,
    TestRxModule::class
])
interface TestApplicationComponent : ApplicationComponent
