package com.example.superagenda.presentation.screens.tasks

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.superagenda.R
import com.example.superagenda.core.navigations.Destinations
import com.example.superagenda.domain.models.Task
import com.example.superagenda.presentation.composables.Navigation
import com.example.superagenda.presentation.composables.NavigationViewModel
import com.example.superagenda.presentation.composables.NewTaskFloatingActionButton
import com.example.superagenda.presentation.composables.TaskCard
import com.example.superagenda.presentation.screens.tasks.composables.EmptyState

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TasksScreen(
   tasksViewModel: TasksViewModel,
   navController: NavController,
   navigationViewModel: NavigationViewModel
) {
   Navigation(
      content = { padding ->
         val pagerState = rememberPagerState { 3 }

         var selectedTab by remember {
            mutableIntStateOf(pagerState.currentPage)
         }

         LaunchedEffect(selectedTab) {
            pagerState.scrollToPage(selectedTab)
         }

         LaunchedEffect(pagerState.currentPage) {
            selectedTab = pagerState.currentPage
         }

         Column(modifier = Modifier.padding(padding)) {
            TabRow(selectedTabIndex = selectedTab) {
               for (index in 0 until pagerState.pageCount) {
                  Tab(
                     selected = true,
                     onClick = { selectedTab = index }
                  ) {
                     when (index) {
                        0 -> Text("Not Started")
                        1 -> Text("Ongoing")
                        2 -> Text("Completed")
                     }
                  }
               }
            }

            HorizontalPager(
               state = pagerState,
               modifier = Modifier.fillMaxSize()
            ) { currentPage ->
               when (currentPage) {
                  0 -> {
                     tasksViewModel.loadTasksNotStarted()
                     TasksNotStarted(tasksViewModel, navController)
                  }

                  1 -> {
                     tasksViewModel.loadTasksOngoing()
                     TasksOngoing(tasksViewModel, navController)
                  }

                  2 -> {
                     tasksViewModel.loadTasksCompleted()
                     TasksCompleted(tasksViewModel, navController)
                  }
               }
            }
         }
      },
      navController = navController,
      topBarTitle = "Tasks",
      floatingActionButton = {
         NewTaskFloatingActionButton {
            navController.navigate(Destinations.NewTask.route)
         }
      },
      navigationViewModel = navigationViewModel
   )
}

@Composable
fun TasksNotStarted(tasksViewModel: TasksViewModel, navController: NavController) {
   val tasksNotStarted: List<Task>? by tasksViewModel.tasksNotStarted.observeAsState()

   LazyColumn(modifier = Modifier.fillMaxSize()) {
      item {
         if (tasksNotStarted == null) {
            EmptyState(message = "Something went wrong", iconId = R.drawable.ic_launcher_foreground)
         } else if (tasksNotStarted!!.isEmpty()) {
            EmptyState(
               message = "Empty and so quiet...",
               iconId = R.drawable.ic_launcher_foreground
            )
         } else {
            for (task in tasksNotStarted!!) {
               TaskCard(task) {
                  tasksViewModel.setTaskToEdit(task)
                  navController.navigate(Destinations.TaskEdit.route)
               }
            }
         }

      }
   }
}

@Composable
fun TasksOngoing(tasksViewModel: TasksViewModel, navController: NavController) {
   val taskOngoing: List<Task>? by tasksViewModel.tasksOngoing.observeAsState()

   LazyColumn(modifier = Modifier.fillMaxSize()) {
      item {
         if (taskOngoing == null) {
            EmptyState(message = "Something went wrong", iconId = R.drawable.ic_launcher_foreground)
         } else if (taskOngoing!!.isEmpty()) {
            EmptyState(
               message = "Empty and so quiet...",
               iconId = R.drawable.ic_launcher_foreground
            )
         } else {
            for (task in taskOngoing!!) {
               TaskCard(task) {
                  tasksViewModel.setTaskToEdit(task)
                  navController.navigate(Destinations.TaskEdit.route)
               }
            }
         }

      }
   }
}

@Composable
fun TasksCompleted(tasksViewModel: TasksViewModel, navController: NavController) {
   val tasksCompleted: List<Task>? by tasksViewModel.tasksCompleted.observeAsState()

   LazyColumn(modifier = Modifier.fillMaxSize()) {
      item {
         if (tasksCompleted == null) {
            EmptyState(message = "Something went wrong", iconId = R.drawable.ic_launcher_foreground)
         } else if (tasksCompleted!!.isEmpty()) {
            EmptyState(
               message = "Empty and so quiet...",
               iconId = R.drawable.ic_launcher_foreground
            )
         } else {
            for (task in tasksCompleted!!) {
               TaskCard(task) {
                  tasksViewModel.setTaskToEdit(task)
                  navController.navigate(Destinations.TaskEdit.route)
               }
            }
         }
      }
   }
}