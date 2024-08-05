package edu.rit.nn7716.steakapp.data.api

import MethodsResponse
import edu.rit.nn7716.steakapp.data.api.model.SteaksResponse
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET


const val BASE_URL = "http://solace.ist.rit.edu/~dk4368/"

interface APIService {
    @GET("data/steaks.json")
    suspend fun getSteaks(): SteaksResponse

    @GET("data/methods.json")
    suspend fun getMethods(): MethodsResponse

    companion object {
        private val interceptor = HttpLoggingInterceptor().apply {
            this.level = HttpLoggingInterceptor.Level.BODY
        }

        private val client: OkHttpClient = OkHttpClient.Builder().apply {
            addInterceptor(interceptor)
        }.build()

        var apiService: APIService? = null

        fun getInstance(): APIService {
            if (apiService == null) {
                apiService = Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(client)
                    .build().create(APIService::class.java)
            }
            return apiService!!
        }
    }
}








