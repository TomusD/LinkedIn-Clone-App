package com.example.front.screens.user

import android.app.Activity
import android.content.Context
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.front.activity.BasicViewModel
import com.example.front.data.ApiClient
import com.example.front.data.response.APIResponse
import com.example.front.data.response.EducationResponse
import com.example.front.data.response.SkillsList
import com.example.front.data.response.WorkResponse
import com.example.front.screens.Subcomponents.Chip
import com.example.front.screens.Subcomponents.modals.EducationModal
import com.example.front.screens.Subcomponents.modals.WorkModal
import com.example.front.screens.Subcomponents.profile.EduInfo
import com.example.front.screens.Subcomponents.profile.WorkInfo
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview
fun ProfileScreen(viewModel: BasicViewModel = viewModel()) {
    val context = LocalContext.current


    LaunchedEffect(Unit) {
        viewModel.fetchWork(context)
        viewModel.fetchEducation(context)
        viewModel.fetchSkills(context)
        viewModel.fetchPublicity(context)
    }

    var workList = viewModel.workList.collectAsState().value
    var eduList = viewModel.educationList.collectAsState().value
    var skillsSet = viewModel.skillsList.collectAsState().value
    var publicityMap = viewModel.publicityMap.collectAsState().value

    var selectedTab by remember { mutableIntStateOf(0) }
    val tabs = listOf("Work", "Education", "Skills")

    Column(modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally) {

        // Padding to move the header down
        Spacer(modifier = Modifier.height(40.dp))

        // Header with Profile title and icon
        TopAppBar(
            title = {
                Text(
                    text = "Profile",
                    fontSize = 5.em,
                    fontWeight = FontWeight.Bold
                )
            },
            navigationIcon = {
                IconButton(onClick = {
                    val activity = (context as? Activity)
                    activity?.finish()
                }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back"
                    )
                }
            },
            colors = TopAppBarDefaults.mediumTopAppBarColors(
                containerColor = Color.Transparent,
                titleContentColor = Color.Black,
                navigationIconContentColor = Color.Black
            ),
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .fillMaxWidth()
        )

        // Custom TabRow
        TabRow(
            selectedTabIndex = selectedTab,
            indicator = { tabPositions ->
                TabRowDefaults.Indicator(
                    Modifier
                        .tabIndicatorOffset(tabPositions[selectedTab])
                        .height(0.dp) // Hide the default indicator
                )
            },
            divider = {
                Divider(color = Color.LightGray, thickness = 1.dp)
            },
            containerColor = Color.Transparent,
        ) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTab == index,
                    onClick = { selectedTab = index },
                    modifier = Modifier
                        .padding(horizontal = 8.dp, vertical = 10.dp)
                        .clip(RoundedCornerShape(5.dp))
                        .background(
                            if (selectedTab == index) Color.Gray else Color.Transparent
                        )
                        .height(40.dp),

                ) {
                    Text(
                        text = title,
                        fontSize = 4.em,
                        color = if (selectedTab == index) Color.White else Color.Gray,
                        modifier = Modifier.padding(vertical = 4.dp)
                    )
                }
            }
        }

//        Divider(color = Color.Red, thickness = 5.dp)

            when (selectedTab) {
                0 -> publicityMap["work"]?.let { WorkExperienceTab(workList, viewModel, it) }
                1 -> publicityMap["education"]?.let { EducationTab(eduList, viewModel, it) }
                2 -> publicityMap["skills"]?.let { SkillsTab(skillsSet, viewModel, it) }
            }
        }
    }


@Composable
fun WorkExperienceTab(workList: List<WorkResponse>, viewModel: BasicViewModel, publicity: Boolean = true) {
    val context = LocalContext.current

    var showDialog by remember { mutableStateOf(false) }
    var isPublic by remember { mutableStateOf(publicity) }

    // Padding to move the header down
    Spacer(modifier = Modifier.height(20.dp))
    ToggleButton(isPublic) {
        viewModel.updatePublicity(context, "work")
        isPublic = !isPublic
        viewModel.updatePublicityToggle("work", isPublic)
    }

    Column (
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()

    ) {
        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = { showDialog = true },
            modifier = Modifier.clip(RoundedCornerShape(10.dp)),
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Black,
                contentColor = Color.White
            )
        ) {

            Text("+ Add Work Experience")
        }

        if (showDialog) {
            WorkModal(
                onDismiss = { showDialog = false },
                onSave = { work ->
                    viewModel.updateWork(work)
                    showDialog = false
                }
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        if (workList.isNotEmpty()) {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(workList) { work ->
                    WorkInfo(work)
                }
            }
        } else {
            Text("No work experience added")
        }
    }
}


@Composable
fun EducationTab(eduList: List<EducationResponse> = mutableListOf(), viewModel: BasicViewModel, publicity: Boolean = true) {
    val context = LocalContext.current
    var localEduList = eduList

    var showDialog by remember { mutableStateOf(false) }
    var isPublic by remember { mutableStateOf(publicity) }

    Spacer(modifier = Modifier.height(20.dp))
    ToggleButton(isPublic) {
        viewModel.updatePublicity(context, "education")
        isPublic = !isPublic
        viewModel.updatePublicityToggle("education", isPublic)
    }

    Column (
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()

    )
    {
        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = { showDialog = true },
            modifier = Modifier.clip(RoundedCornerShape(10.dp)),
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Black,
                contentColor = Color.White
            )
        ) {

            Text("+ Add Education")
        }

        if (showDialog) {
            EducationModal(
                onDismiss = { showDialog = false },
                onSave = { education ->
                    Log.d("MYTEST", "Saved Education: $education")
                    viewModel.updateEducation(education)
                    showDialog = false
                }
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

//        var eduList2 = mutableListOf(EducationResponse(1, "UoA", null, "ROCKET", "2019-03-02", null), )
        if (localEduList.isNotEmpty()) {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(localEduList) { edu ->
                    EduInfo(edu)
                }
            }
        } else {
            Text("No education added")
        }
    }
}


@OptIn(ExperimentalLayoutApi::class)
@Composable
fun SkillsTab(skillsSet: List<String>, viewModel: BasicViewModel, publicity: Boolean = true) {
    val context = LocalContext.current
    var isPublic by remember { mutableStateOf(publicity) }

    var selectedSkills by remember { mutableStateOf(skillsSet) }

    val languageSkills  = mutableListOf("Python", "JavaScript", "Java", "C++", "C#", "Ruby", "Go", "Swift", "Kotlin", "PHP")
    val softSkills  = mutableListOf("Communication", "Teamwork", "Problem Solving", "Adaptability", "Time Management", "Critical Thinking", "Leadership", "Creativity", "Emotional Intelligence", "Conflict Resolution")
    val technicalSkills  = mutableListOf("Data Analysis", "Project Management", "Technical Writing", "SEO", "Database Management", "Cybersecurity", "UI/UX Design", "Cloud Computing", "Machine Learning", "DevOps")
    val availableSkills = languageSkills + softSkills + technicalSkills

    var selectedSkill by remember {
        mutableStateOf<String?>(null)
    }

    var expanded by remember { mutableStateOf(false) }

    Spacer(modifier = Modifier.height(20.dp))
    ToggleButton(isPublic) {
        viewModel.updatePublicity(context, "skills")
        isPublic = !isPublic
        viewModel.updatePublicityToggle("skills", isPublic)
    }

    Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 5.dp)) {
        // Dropdown to select skills

            Row (horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                Button(
                    onClick = { expanded = true },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Black,
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.fillMaxWidth(0.5f)
                ) {
                    Text(text = "+ Add skills")

                }
                Spacer(modifier = Modifier.width(10.dp))

                Button(
                    onClick = {
                        updateSkills(selectedSkills, context)
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(6, 214, 160),
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.fillMaxWidth(1f)

                ) {
                    Text(text = "Save")

                }
            }

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                availableSkills.forEach { skill ->
                    DropdownMenuItem(
                        text = { Text(text = skill) },
                        onClick = {
                            if (skill !in selectedSkills) {
                                selectedSkills = selectedSkills + skill
                                selectedSkill = skill
                                Log.d("MYTEST", selectedSkills.toString())
                            }
                            expanded = false
                        }
                    )
                }
            }
        }


    val scrollState = rememberScrollState()

    FlowRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(15.dp)
            .verticalScroll(scrollState),
        horizontalArrangement = Arrangement.spacedBy(5.dp)

    ) {
        selectedSkills.forEach { skill ->
            key(skill) { // Use `key` to ensure stable identity for each skill
                Chip(
                    text = " $skill",
                    onDelete = {
                        selectedSkills = selectedSkills - skill
                        if (selectedSkills.isEmpty()) {
                            selectedSkill = null
                        }
                    }
                )
            }
        }
    }
}


fun updateSkills(skillsSet: List<String>, context: Context) {
        val apiClient = ApiClient()

        val call = apiClient.getApiService(context).updateSkills(SkillsList(skillsSet))

        call.enqueue(object : Callback<APIResponse> {
            override fun onResponse(call: Call<APIResponse>, response: Response<APIResponse>) {
                if (response.isSuccessful) {
                    val res = response.body()
                    Log.d("MYTEST", res.toString())

                } else {
                    Log.e("MYTEST", "RESPONSE NOT SUCCESSFUL: $response")
                    // Handle error
                }
            }
            override fun onFailure(call: Call<APIResponse>, t: Throwable) {
                Log.e("MYTEST", "TOTAL FAILURE")
            }
        })
}


@Composable
fun ToggleButton(isPublic: Boolean, onToggle: () -> Unit) {
    Row (horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically) {
        Text(
            text = "This field is " + (if (isPublic) "public  " else "private  "),
            fontSize = 4.em
        )
        CustomSwitch(
            checked = isPublic,
            onCheckedChange = { onToggle() },

            )
    }
}

@Composable
fun CustomSwitch(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    // Adjusting the size by using Box with padding
    Box(
        modifier = Modifier
            .size(width = 50.dp, height = 40.dp) // Adjust the size of the switch
            .padding(6.dp)
    ) {
        Switch(

            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color(6, 214, 160),  // Color when checked
                uncheckedThumbColor = Color.Gray, // Color when unchecked
                checkedTrackColor = Color(6, 214, 160).copy(alpha = 0.5f), // Track color when checked
                uncheckedTrackColor = Color.Gray.copy(alpha = 0.5f) // Track color when unchecked
            ),
            modifier = Modifier.fillMaxSize() // Fills the size of the Box
        )
    }
}
