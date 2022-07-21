package com.example.nycschools.di

import android.app.Activity
import com.example.nycschools.DetailActivity
import com.example.nycschools.MainActivity
import dagger.Component
import javax.inject.Singleton
//Dagger 2 injection for retrogit object

@Singleton
@Component(
    modules = [
        ApiModule::class
    ]
)
interface AppComponent {
    // Create a method with the class that has the field you want to inject as an argument
    fun inject(activity: MainActivity)
    fun inject(activity: DetailActivity)
}