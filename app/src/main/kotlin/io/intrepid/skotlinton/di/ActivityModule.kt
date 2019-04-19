package io.intrepid.skotlinton.di

import dagger.Module
import dagger.Provides
import io.intrepid.skotlinton.base.CommonViewModelDependencies
import io.intrepid.skotlinton.logging.CrashReporter
import io.intrepid.skotlinton.rest.RestApi
import io.reactivex.Scheduler

@Module
class ActivityModule {

    @ActivityScope
    @Provides
    fun provideCommonViewModelDependencies(
            @IoScheduler ioScheduler: Scheduler,
            @UiScheduler uiScheduler: Scheduler,
            restApi: RestApi,
            crashReporter: CrashReporter
    ): CommonViewModelDependencies {
        return CommonViewModelDependencies(ioScheduler, uiScheduler, restApi, crashReporter)
    }
}
