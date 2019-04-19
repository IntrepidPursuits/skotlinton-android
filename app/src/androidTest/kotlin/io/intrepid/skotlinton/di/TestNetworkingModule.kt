package io.intrepid.skotlinton.di

import com.nhaarman.mockitokotlin2.mock
import dagger.Module
import dagger.Provides
import io.intrepid.skotlinton.rest.RestApi

@Module
object TestNetworkingModule {

    var restApi: RestApi = mock()

    @Provides
    fun provideRestApi(): RestApi {
        return restApi
    }

    fun reset() {
        restApi = mock()
    }
}
