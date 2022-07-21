package com.example.nycschools.network

import com.example.nycschools.data.SchoolInfo
import com.example.nycschools.data.Schools
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

//Retrofit instant to get data from server
interface ApiService {

    @GET("s3k6-pzi2.json")
    suspend fun getAllSchoolInformation(): List<Schools>

    @GET("f9bf-2cp4.json")
    suspend fun getSchoolInformation(@Query("dbn") dbn: String): List<SchoolInfo>

    companion object {
        fun getInstance(): ApiService {
            var apiService: ApiService? = null

            if (apiService == null) {
                apiService = Retrofit.Builder()
                    .baseUrl("https://data.cityofnewyork.us/resource/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build().create(ApiService::class.java)
            }
            return apiService!!
        }
    }
}