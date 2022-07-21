package com.example.nycschools.viewModels

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nycschools.data.SchoolInfo
import com.example.nycschools.data.Schools
import com.example.nycschools.repository.Repository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

//view model to get the requested data
class NYCSchoolViewModel(
    private val repository: Repository,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : ViewModel() {

    var listOfSchools: List<Schools> by mutableStateOf(listOf())
    var schoolInfo by mutableStateOf(SchoolInfo("", "", "", "", ""))
    var progress by mutableStateOf(true)

    var exception = CoroutineExceptionHandler { _, e ->
        throw Exception(e)
    }

    //get the all the school information
    fun getAllSchools() {
        viewModelScope.launch(dispatcher + exception) {
            val allSchools = repository.getAllSchools()
            listOfSchools = allSchools
            progress = false
        }
    }

    //get only school info using its dbn
    fun getSchoolInfo(dbn: String) {
        viewModelScope.launch(dispatcher + exception) {
            val listSchoolInfo: List<SchoolInfo> = repository.getSchoolInfo(dbn)
            if (listSchoolInfo.size > 0) {
                schoolInfo = listSchoolInfo[0]
                Log.d("Kunal", " called again")
            }
            Log.d("Kunal", " called again 1")
            progress = false
        }
    }
}