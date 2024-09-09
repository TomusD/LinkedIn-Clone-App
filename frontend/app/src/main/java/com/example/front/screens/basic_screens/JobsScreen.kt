package com.example.front.screens.basic_screens

import BasicViewModel
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.front.screens.Subcomponents.jobs_tabs.JobsTab
import com.example.front.screens.Subcomponents.jobs_tabs.MyJobsTab
import com.example.front.ui.theme.FrontEndTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JobsScreen(navController: NavController, viewModel: BasicViewModel = viewModel()) {
    FrontEndTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            val context = LocalContext.current


            LaunchedEffect(Unit) {
                viewModel.fetchWork(context)
            }

//            var workList = viewModel.workList.collectAsState().value

            var selectedTab by remember { mutableIntStateOf(0) }
            val tabs = listOf("Jobs", "My Jobs")

            Column(modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally) {

                // Padding to move the header down
                Spacer(modifier = Modifier.height(40.dp))

                // Header with Profile title and icon
//                TopAppBar(
//                    title = {
//                        Text(
//                            text = "Profile",
//                            fontSize = 5.em,
//                            fontWeight = FontWeight.Bold
//                        )
//                    },
//                    navigationIcon = {
//                        IconButton(onClick = {
//                            val activity = (context as? Activity)
//                            // Call this to finish the current activity
//                            activity?.finish()
//                        }) {
//                            Icon(
//                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
//                                contentDescription = "Back"
//                            )
//                        }
//                    },
//                    colors = TopAppBarDefaults.mediumTopAppBarColors(
//                        containerColor = Color.Transparent,
//                        titleContentColor = Color.Black,
//                        navigationIconContentColor = Color.Black
//                    ),
//                    modifier = Modifier
//                        .padding(horizontal = 16.dp, vertical = 8.dp)
//                        .fillMaxWidth()
//                )

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
                    0 -> JobsTab()
                    1 -> MyJobsTab()
                }
            }
        }
    }
}
