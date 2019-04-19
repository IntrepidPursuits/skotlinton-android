package io.intrepid.skotlinton.di

import com.nhaarman.mockitokotlin2.mock
import dagger.Module
import dagger.Provides
import io.intrepid.skotlinton.logging.CrashReporter
import io.intrepid.skotlinton.settings.UserSettings
import io.intrepid.skotlinton.utils.TimeProvider

@Module
object TestApplicationModule {
    var userSettings: UserSettings = mock()
    var timeProvider: TimeProvider = mock()

    @Provides
    fun provideUserSettings(): UserSettings {
        return userSettings
    }

    @Provides
    fun provideTimeProvider(): TimeProvider {
        return timeProvider
    }

    @Provides
    fun provideCrashReporter(): CrashReporter {
        return mock()
    }

    fun reset() {
        userSettings = mock()
        timeProvider = mock()
    }
}
