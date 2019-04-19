package io.intrepid.skotlinton.di

import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [
    ApplicationModule::class,
    NetworkingModule::class,
    RxModule::class
])
interface ApplicationComponent {
    fun activityComponent(activityModule: ActivityModule): ActivityComponent
}
