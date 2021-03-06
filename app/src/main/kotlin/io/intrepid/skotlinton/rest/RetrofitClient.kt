package io.intrepid.skotlinton.rest

import androidx.annotation.VisibleForTesting
import io.intrepid.skotlinton.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import timber.log.Timber
import java.util.concurrent.TimeUnit

object RetrofitClient {

    // TODO: change this to a real endpoint
    private const val BASE_URL = "https://api.ipify.org/"
    private const val CONNECTION_TIMEOUT = 30L

    val restApi: RestApi by lazy { createRestApi(BASE_URL) }

    @VisibleForTesting
    internal fun getTestApi(baseUrl: String): RestApi = createRestApi(baseUrl)

    private fun createRestApi(baseUrl: String): RestApi {
        val builder = OkHttpClient.Builder()
        if (BuildConfig.LOG_CONSOLE) {
            builder.addInterceptor(HttpLoggingInterceptor({ Timber.v(it) }).setLevel(HttpLoggingInterceptor.Level.BODY))
        }

        val httpClient = builder.connectTimeout(CONNECTION_TIMEOUT, TimeUnit.SECONDS).build()
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(httpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(RestApi::class.java)
    }
}
