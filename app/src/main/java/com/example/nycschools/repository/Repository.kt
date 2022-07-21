package com.example.nycschools.repository

import com.example.nycschools.data.SchoolInfo
import com.example.nycschools.data.Schools
import com.example.nycschools.network.ApiService

//Following MVVM patter to get into from REST api
class Repository(private val apiService: ApiService) {

    suspend fun getAllSchools(): List<Schools> {
        return apiService.getAllSchoolInformation()
    }

    suspend fun getSchoolInfo(dbn: String): List<SchoolInfo> {
        return apiService.getSchoolInformation(dbn)
    }
}