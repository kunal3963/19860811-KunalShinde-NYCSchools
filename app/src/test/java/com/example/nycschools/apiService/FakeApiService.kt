package com.example.nycschools.apiService

import com.example.nycschools.data.SchoolInfo
import com.example.nycschools.data.Schools
import com.example.nycschools.network.ApiService
import retrofit2.http.GET
import retrofit2.http.Query

class FakeApiService : ApiService{
    suspend override fun getAllSchoolInformation(): List<Schools> {
        return mutableListOf(
            Schools("test1", "test School1", "12345", "www.fake1.com"),
            Schools("test2", "test School2", "67890", "www.fake2.com")
        )
    }

    suspend override fun getSchoolInformation(@Query("dbn") dbn: String): List<SchoolInfo> {
        return mutableListOf(
            SchoolInfo("test1", "test School1", "11111", "22222","33333"),
        )
    }
}