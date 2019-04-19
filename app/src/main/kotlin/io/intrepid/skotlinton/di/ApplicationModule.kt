package io.intrepid.skotlinton.di

import dagger.Module
import dagger.Provides
import io.intrepid.skotlinton.SkotlintonApplication
import io.intrepid.skotlinton.logging.CrashReporter
import io.intrepid.skotlinton.logging.CrashlyticsReporter
import io.intrepid.skotlinton.settings.SharedPreferencesManager
import io.intrepid.skotlinton.settings.UserSettings
import io.intrepid.skotlinton.utils.SystemTimeProvider
import io.intrepid.skotlinton.utils.TimeProvider
import javax.inject.Singleton

@Module
class ApplicationModule(private val application: SkotlintonApplication) {
    @Singleton
    @Provides
    fun provideUserSettings(): UserSettings {
        return SharedPreferencesManager.getInstance(application)
    }

    @Singleton
    @Provides
    fun provideTimeProvider(): TimeProvider {
        return SystemTimeProvider
    }

    @Singleton
    @Provides
    fun provideCrashReporter(): CrashReporter {
        return CrashlyticsReporter
    }
}
