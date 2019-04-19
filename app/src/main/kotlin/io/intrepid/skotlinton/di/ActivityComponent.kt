package io.intrepid.skotlinton.di

import dagger.Subcomponent
import io.intrepid.skotlinton.screens.example1.Example1Activity
import io.intrepid.skotlinton.screens.example2.Example2Fragment

@ActivityScope
@Subcomponent(modules = [
    ActivityModule::class
])
interface ActivityComponent {
    fun inject(activity: Example1Activity)
    fun inject(fragment: Example2Fragment)
}
