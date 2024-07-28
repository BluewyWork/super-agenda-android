package com.example.superagenda.presentation.screens.tasks

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TasksScreen()
{
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

   Column {
      TabRow(selectedTabIndex = selectedTab) {
         for (index in 0 until pagerState.pageCount)
         {
            Tab(
               selected = true,
               onClick = { selectedTab = index }
            ) {
               when (index)
               {
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
         when (currentPage)
         {
            0 -> TasksNotStarted()
            1 -> TasksOngoing()
            2 -> TasksCompleted()
         }
      }
   }
}

@Composable
fun TasksNotStarted()
{
   Text("test1")
}

@Composable
fun TasksOngoing()
{
   Text("test2")
}

@Composable
fun TasksCompleted()
{
   Text("test3")
}