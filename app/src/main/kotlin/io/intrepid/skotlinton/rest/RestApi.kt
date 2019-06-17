package io.intrepid.skotlinton.rest

import io.intrepid.skotlinton.models.IpModel
import io.reactivex.Single
import retrofit2.http.GET

interface RestApi {
    @GET("/?format=json")
    suspend fun getMyIp(): IpModel
}
