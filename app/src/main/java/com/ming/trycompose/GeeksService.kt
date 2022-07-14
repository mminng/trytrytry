package com.ming.trycompose

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.FieldMap
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

/**
 * Created by zh on 2021/8/17.
 */
interface GeeksService {

//    @FormUrlEncoded
//    @POST("v2/feed")
//    suspend fun login(
//        @Field("date") date: Long,
//        @Field("num") num: Int
//    ): GeeksResponse

    @FormUrlEncoded
    @POST("v2/feed")
    suspend fun login(@FieldMap map: Map<String, String>): Response<GeeksResponse>

    companion object {
        private const val BASE_URL = "http://baobab.kaiyanapp.com/api/"

        fun create(): GeeksService {
            val logger: HttpLoggingInterceptor = HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            }

            val client: OkHttpClient = OkHttpClient.Builder()
                .addInterceptor(logger)
                .build()

            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(GeeksService::class.java)
        }
    }

}