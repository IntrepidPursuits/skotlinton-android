package io.intrepid.skotlinton.di

import dagger.Module
import dagger.Provides
import io.intrepid.skotlinton.rest.RestApi
import io.intrepid.skotlinton.rest.RetrofitClient

@Module
class NetworkingModule {
    @Provides
    fun provideRestApi(): RestApi {
        return RetrofitClient.restApi
    }
}
