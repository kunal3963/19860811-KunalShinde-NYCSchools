package com.example.nycschools

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModelProvider
import com.example.nycschools.data.SchoolInfo
import com.example.nycschools.network.ApiService
import com.example.nycschools.repository.Repository
import com.example.nycschools.ui.theme.NYCSchoolsTheme
import com.example.nycschools.utils.Constants
import com.example.nycschools.utils.Constants.Companion.ERROR_MESSAGE
import com.example.nycschools.utils.NetworkUtils
import com.example.nycschools.viewModels.NYCSchoolFactory
import com.example.nycschools.viewModels.NYCSchoolViewModel

class DetailActivity : ComponentActivity() {
    lateinit var nycViewModel: NYCSchoolViewModel
    lateinit var repository: Repository
    lateinit var apiService: ApiService
    lateinit var schoolName: String
    lateinit var dbn: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dbn = intent.getStringExtra(Constants.DBN_MESSAGE)!!
        schoolName = intent.getStringExtra(Constants.SCHOOL_NAME_MESSAGE)!!
        setContent {
            NYCSchoolsTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    topBar = { TopAppBar(title = { Text("School Information") }) },
                    backgroundColor = MaterialTheme.colors.background
                ) {
                    init()
                    showSchoolInformation()
                }
            }
        }
    }

    @Composable
    fun init() {
        apiService = ApiService.getInstance()
        repository = Repository(apiService)
        nycViewModel = ViewModelProvider(
            this,
            NYCSchoolFactory(repository)
        ).get(NYCSchoolViewModel::class.java)
    }

    @Composable
    fun showSchoolInformation() {
        if (NetworkUtils.isOnline(this)) {
            showSchool(nycViewModel.schoolInfo, nycViewModel.progress)
            nycViewModel.getSchoolInfo(dbn)
        } else {
            Toast.makeText(this, Constants.NETWORK_MESSAGE, Toast.LENGTH_SHORT).show()
        }
    }

    //Show SAT data on the view from SchoolInfo object
    @Composable
    fun showSchool(schoolInfo: SchoolInfo, status: Boolean) {
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
        } else if (schoolInfo.dbn != "") {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp)
            ) {
                Column(
                    modifier = Modifier.padding(5.dp),
                    horizontalAlignment = Alignment.Start
                ) {
                    Text(
                        text = "School Name :" + schoolInfo.school_name,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.caption,
                        modifier = Modifier.padding(bottom = 5.dp)
                    )
                    Text(
                        text = "SAT Data :",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.caption,
                        modifier = Modifier.padding(bottom = 5.dp)
                    )
                    Text(
                        text = "Reading Average: " + schoolInfo.sat_critical_reading_avg_score,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Normal,
                        style = MaterialTheme.typography.caption,
                        modifier = Modifier.padding(bottom = 5.dp)
                    )
                    Text(
                        text = "Math Average: " + schoolInfo.sat_math_avg_score,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Normal,
                        style = MaterialTheme.typography.caption,
                        modifier = Modifier.padding(bottom = 5.dp)
                    )
                    Text(
                        text = "Write Average: " + schoolInfo.sat_writing_avg_score,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Normal,
                        style = MaterialTheme.typography.caption,
                        modifier = Modifier.padding(bottom = 5.dp)
                    )
                }
            }
        } else if (schoolInfo.dbn == "") {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp)
            ) {
                Column(
                    modifier = Modifier.padding(5.dp),
                    horizontalAlignment = Alignment.Start
                ) {
                    Text(
                        text = ERROR_MESSAGE.plus(schoolName),
                        fontSize = 25.sp,
                        fontWeight = FontWeight.Normal,
                        style = MaterialTheme.typography.caption,
                        modifier = Modifier.padding(bottom = 5.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun Greeting2(name: String) {
    Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview2() {
    NYCSchoolsTheme {
        Greeting2("Android")
    }
}