package io.intrepid.skotlinton.di

import android.os.AsyncTask
import dagger.Module
import dagger.Provides
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Singleton

@Module
object TestRxModule {
    @Provides
    @Singleton
    @IoScheduler
    fun provideIoScheduler(): Scheduler {
        // using AsyncTask executor since Espresso automatically waits for it to clear before proceeding
        return Schedulers.from(AsyncTask.SERIAL_EXECUTOR)
    }

    @Provides
    @Singleton
    @UiScheduler
    fun provideUiScheduler(): Scheduler {
        return AndroidSchedulers.mainThread()
    }
}
