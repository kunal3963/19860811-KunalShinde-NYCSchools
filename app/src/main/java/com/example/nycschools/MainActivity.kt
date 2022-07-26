package com.example.nycschools

import com.example.nycschools.di.DaggerAppComponent
import com.example.nycschools.di.ApiModule
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModelProvider
import com.example.nycschools.data.Schools
import com.example.nycschools.network.ApiService
import com.example.nycschools.repository.Repository
import com.example.nycschools.ui.theme.NYCSchoolsTheme
import com.example.nycschools.utils.Constants
import com.example.nycschools.utils.NetworkUtils
import com.example.nycschools.viewModels.NYCSchoolFactory
import com.example.nycschools.viewModels.NYCSchoolViewModel
import javax.inject.Inject

class MainActivity : ComponentActivity() {

    lateinit var nycViewModel: NYCSchoolViewModel
    var showIndex by mutableStateOf(-1)

    @Inject
    lateinit var apiService: ApiService
    @Inject
    lateinit var repository: Repository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NYCSchoolsTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    topBar = { TopAppBar(title = { Text("NYC Schools") }) },
                    backgroundColor = MaterialTheme.colors.background
                ) {
                    //init all the instance of the application
                    init()
                    //check if showIndex value is changed, it it is changed then call next activity
                    if (showIndex != -1) {
                        startSchoolInfoActivity()
                    }
                }
            }
        }
    }

    @Composable
    fun init() {
        val appComponent = DaggerAppComponent.builder()
            .apiModule(ApiModule(Constants.BASE_URL))
            .build()
        appComponent.inject(this)
        nycViewModel = ViewModelProvider(
            this,
            NYCSchoolFactory(repository)
        ).get(NYCSchoolViewModel::class.java)

        if (NetworkUtils.isOnline(this)) {
            nycViewModel.getAllSchools()
            ListSchool(nycViewModel.listOfSchools, nycViewModel.progress)
        } else {
            Toast.makeText(this, Constants.NETWORK_MESSAGE, Toast.LENGTH_SHORT).show()
        }
    }

    //show index should be reset so that once our activity get resume then click should be worked
    override fun onResume() {
        super.onResume()
        showIndex = -1
    }

    @Composable
    fun startSchoolInfoActivity() {
        Log.d("Kunal", " called startSchoolInfoActivity")
        val intent = Intent(this, DetailActivity::class.java)
        intent.putExtra(
            Constants.DBN_MESSAGE,
            nycViewModel.listOfSchools[showIndex].dbn
        )
        intent.putExtra(
            Constants.SCHOOL_NAME_MESSAGE,
            nycViewModel.listOfSchools[showIndex].school_name
        )
        startActivity(intent)
    }

    //Show list of schools in the recycler view using android compose
    @Composable()
    fun ListSchool(schoolList: List<Schools>, status: Boolean) {
        //If status is true then we need to show progress till we get response
        if (status) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CircularProgressIndicator(
                    color = MaterialTheme.colors.secondary,
                    strokeWidth = 7.dp
                )
            }
        } else {
            LazyColumn() {
                itemsIndexed(schoolList) { index, item ->
                    SchoolItem(item, index)
                }
            }
        }
    }

    //Card view to show one item on list
    @Composable
    fun SchoolItem(school: Schools, index: Int) {
        Card(
            backgroundColor = MaterialTheme.colors.secondary,
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp, 8.dp)
                .clickable { showIndex = index },
            elevation = 8.dp
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp)
            ) {
                Text(
                    text = school.school_name,
                    fontSize = 25.sp,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.caption,
                    modifier = Modifier.padding(bottom = 5.dp)
                )
                Text(
                    text = "Phone number: " + school.phone_number,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Normal,
                    style = MaterialTheme.typography.subtitle1,
                    modifier = Modifier.padding(bottom = 5.dp)
                )
                Text(
                    text = "Web :" + school.website,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Normal,
                    style = MaterialTheme.typography.subtitle2,
                    modifier = Modifier.padding(bottom = 5.dp)
                )
            }
        }
    }
}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    NYCSchoolsTheme {
        Greeting("Android")
    }
}