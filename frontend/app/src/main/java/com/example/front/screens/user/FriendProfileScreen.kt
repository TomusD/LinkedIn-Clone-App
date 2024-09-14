package com.example.front.screens.user

import android.app.Activity
import android.widget.Toast
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.front.R
import com.example.front.activity.FriendsViewModel
import com.example.front.data.base.User
import com.example.front.data.response.EducationResponse
import com.example.front.data.response.WorkResponse
import com.example.front.screens.Subcomponents.profile.EduInfo
import com.example.front.screens.Subcomponents.profile.WorkInfo

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FriendProfileScreen(user: User, viewModel: FriendsViewModel = viewModel()) {
    val context = LocalContext.current

    LaunchedEffect(Unit) {
//        viewModel.getUser(context, userId)
        viewModel.getPublicInfo(context, user.id.toInt())
    }


//    var user = viewModel.user.collectAsState().value
    val userInfo = viewModel.userInfo.collectAsState().value

    var selectedTab by remember { mutableIntStateOf(0) }
    val tabs = listOf("Work", "Education", "Skills")

    fun titlize(str: String) = str.lowercase().replaceFirstChar(Char::titlecase)

    Column(modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally) {

        // Padding to move the header down
        Spacer(modifier = Modifier.height(40.dp))

        // Header with Profile title and icon
        TopAppBar(
            title = {
                Row(horizontalArrangement = Arrangement.Absolute.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {

                    AsyncImage(model = user.imagePath, contentDescription = "user-profile",
                            modifier = Modifier.clip(RoundedCornerShape(10.dp)).size(80.dp))
                    Text(
                        text = "${titlize(user.name)} ${titlize(user.surname)}",
                        fontSize = 5.em,
                        fontWeight = FontWeight.Bold
                    )


                    if (userInfo?.is_friend == false) {
                        IconButton(onClick = {
                            viewModel.sendFriendRequest(context, user.id.toInt())
                            Toast.makeText(context, "Friend request sent!", Toast.LENGTH_SHORT).show()

                        }) {
                            Icon(painter = painterResource(R.drawable.addfriend_icon),
                                contentDescription = "",
                                Modifier.size(25.dp))
                        }

                    } else {
                        Box(modifier = Modifier.background(Color(6, 214, 160), shape = RoundedCornerShape(10.dp))) {
                            Text(text = "Friends", color = Color.White, fontSize = 3.em, modifier = Modifier.padding(5.dp))
                        }
                    }
                }
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
                HorizontalDivider(thickness = 1.dp, color = Color.LightGray)
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

            when (selectedTab) {
                0 -> FriendWorkExperienceTab(userInfo?.work!!.workList, userInfo.is_friend)
                1 -> FriendEducationTab(userInfo?.education!!.eduList, userInfo.is_friend)
                2 -> FriendSkillsTab(userInfo?.skills!!.skills, userInfo.is_friend)
            }
        }
    }


@Composable
fun FriendWorkExperienceTab(workList: List<WorkResponse>, isFriend: Boolean? = false) {
    Column (
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()

    ) {
        Spacer(modifier = Modifier.height(20.dp))

        if (isFriend == true) {
            if (workList.isEmpty()) {
                Text(text = "User hasn't added any work experience")
            } else {
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(workList) { work ->
                        WorkInfo(work)
                    }
                }
            }

        } else {
            if (workList.isEmpty()) {
                Text(text = "Work Experience not available", modifier = Modifier.padding(5.dp))
            } else {
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(workList) { work ->
                        WorkInfo(work)
                    }
                }
            }
        }
    }
}


@Composable
fun FriendEducationTab(eduList: List<EducationResponse> = mutableListOf(), isFriend: Boolean? = false) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
    ) {
        Spacer(modifier = Modifier.height(20.dp))


        if (isFriend == true) {
            if (eduList.isEmpty()) {
                Text(text = "User hasn't added any education")
            } else {
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(eduList) { edu ->
                        EduInfo(edu)
                    }
                }
            }

        } else {

            if (eduList.isEmpty()) {
                Text(text = "Education not available", modifier = Modifier.padding(5.dp))
            } else {
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(eduList) { edu ->
                        EduInfo(edu)
                    }
                }
            }
        }
    }
}


@OptIn(ExperimentalLayoutApi::class)
@Composable
fun FriendSkillsTab(skillsSet: List<String>, isFriend: Boolean? = false) {
    Spacer(modifier = Modifier.height(20.dp))
    val scrollState = rememberScrollState()

    FlowRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(15.dp)
            .verticalScroll(scrollState),
        horizontalArrangement = Arrangement.spacedBy(5.dp),
        verticalArrangement = Arrangement.spacedBy(5.dp)
    ) {

        if (isFriend==true) {

            if (skillsSet.isEmpty()) {
                Text(text = "User hasn't added any skills")
            } else {
                skillsSet.forEach { skill ->
                    key(skill) { // Use `key` to ensure stable identity for each skill
                        Box(modifier = Modifier.background(Color(255, 209, 102), shape = RoundedCornerShape(5.dp))) {
                            Text(text = skill, modifier = Modifier.padding(5.dp))
                        }
                    }
                }
            }

        } else {

            if (skillsSet.isEmpty()) {
                Text(text = "Skills not available", modifier = Modifier.padding(5.dp))
            } else {
                skillsSet.forEach { skill ->
                    key(skill) { // Use `key` to ensure stable identity for each skill
                        Box(modifier = Modifier.background(Color(255, 209, 102))) {
                            Text(text = " $skill", modifier = Modifier.padding(5.dp))
                        }
                    }
                }
            }

        }
    }
}
