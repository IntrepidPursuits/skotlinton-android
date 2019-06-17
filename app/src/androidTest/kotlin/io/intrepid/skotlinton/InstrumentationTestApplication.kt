package io.intrepid.skotlinton

import android.os.AsyncTask
import io.intrepid.skotlinton.base.ViewModelConfiguration
import io.intrepid.skotlinton.rest.RestApi
import io.intrepid.skotlinton.rest.RetrofitClient
import io.intrepid.skotlinton.settings.SharedPreferencesManager
import io.intrepid.skotlinton.settings.UserSettings
import io.mockk.mockk
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.MainScope

class InstrumentationTestApplication : SkotlintonApplication() {
    override fun getViewModelConfiguration(): ViewModelConfiguration {
        return ViewModelConfiguration(
            // using AsyncTask executor since Espresso automatically waits for it to clear before proceeding
            Schedulers.from(AsyncTask.SERIAL_EXECUTOR),
            AndroidSchedulers.mainThread(),
            userSettingsOverride ?: SharedPreferencesManager.getInstance(this),
            restApiOverride ?: RetrofitClient.restApi,
            mockk(),
            mockk(),
            MainScope()
        )
    }

    override fun setupLeakCanary() {
        // noop, we don't want LeakCanary in UI tests
    }

    companion object {
        private var restApiOverride: RestApi? = null
        private var userSettingsOverride: UserSettings? = null

        fun overrideRestApi(restApi: RestApi) {
            restApiOverride = restApi
        }

        fun clearRestApiOverride() {
            restApiOverride = null
        }

        fun overrideUserSettings(userSettings: UserSettings) {
            userSettingsOverride = userSettings
        }

        fun clearUserSettingsOverride() {
            userSettingsOverride = null
        }
    }
}
