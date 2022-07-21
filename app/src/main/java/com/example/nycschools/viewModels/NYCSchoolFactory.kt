package com.example.nycschools.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.nycschools.repository.Repository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import java.lang.IllegalArgumentException

//Factory to inject repository object and dispatcher
class NYCSchoolFactory(
    private val repository: Repository,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(NYCSchoolViewModel::class.java)) {
            return NYCSchoolViewModel(repository, dispatcher) as T
        } else {
            throw IllegalArgumentException()
        }
    }
}