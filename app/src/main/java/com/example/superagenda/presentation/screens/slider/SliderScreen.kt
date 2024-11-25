package com.example.superagenda.presentation.screens.slider

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.superagenda.R
import com.example.superagenda.presentation.Destinations

@Composable
fun SliderScreen(navController: NavController) {
   Slider(navController)
}

@Composable
fun Slider(navController: NavController) {
   val pagerState = rememberPagerState { 3 }

   var selectedPage by remember {
      mutableIntStateOf(pagerState.currentPage)
   }

   LaunchedEffect(selectedPage) {
      pagerState.scrollToPage(selectedPage)
   }

   LaunchedEffect(pagerState.currentPage) {
      selectedPage = pagerState.currentPage
   }

   Column(
      modifier = Modifier.fillMaxSize()
   ) {
      Box(
         modifier = Modifier.weight(1f) // This ensures the pager takes up the available space
      ) {
         HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxSize()
         ) { currentPage ->
            when (currentPage) {
               0 -> {
                  Box(modifier = Modifier.fillMaxSize()) {
                     Image(
                        painter = painterResource(id = R.drawable.first),
                        contentDescription = null,
                        contentScale = ContentScale.Fit,
                        modifier = Modifier.fillMaxSize()
                     )

                     Column(
                        modifier = Modifier
                           .fillMaxSize(),


                        ) {
                        Column(
                           modifier = Modifier.fillMaxWidth(),
                           verticalArrangement = Arrangement.Center,
                           horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                           Text("Super Agenda", fontSize = 36.sp)
                           Text("Kotlin", fontSize = 20.sp)
                        }

                        Column(
                           modifier = Modifier
                              .fillMaxSize()
                              .padding(bottom = 16.dp),

                           verticalArrangement = Arrangement.Bottom,
                           horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                           Button(
                              onClick = {
                                 selectedPage = 1
                              },
                              modifier = Modifier
                                 .fillMaxWidth()
                                 .padding(start = 16.dp, end = 16.dp)
                           ) {
                              Text("Next")
                           }
                        }
                     }
                  }
               }

               1 -> {
                  Box(modifier = Modifier.fillMaxSize()) {
                     Image(
                        painter = painterResource(id = R.drawable.second),
                        contentDescription = null,
                        contentScale = ContentScale.Fit,
                        modifier = Modifier.fillMaxSize()
                     )

                     Column(
                        modifier = Modifier
                           .fillMaxSize(),


                        ) {
                        Column(
                           modifier = Modifier.fillMaxWidth(),
                           verticalArrangement = Arrangement.Center,
                           horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                           Text("Tareas", fontSize = 36.sp)
                           Text("Magia", fontSize = 20.sp)
                        }

                        Column(
                           modifier = Modifier
                              .fillMaxSize()
                              .padding(bottom = 16.dp),

                           verticalArrangement = Arrangement.Bottom,
                           horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                           Button(
                              onClick = {
                                 selectedPage = 2
                              },
                              modifier = Modifier
                                 .fillMaxWidth()
                                 .padding(start = 16.dp, end = 16.dp)
                           ) {
                              Text("Next")
                           }
                        }
                     }
                  }
               }

               2 -> {
                  Box(modifier = Modifier.fillMaxSize()) {
                     Image(
                        painter = painterResource(id = R.drawable.third),
                        contentDescription = null,
                        contentScale = ContentScale.Fit,
                        modifier = Modifier.fillMaxSize()
                     )

                     Column(
                        modifier = Modifier
                           .fillMaxSize(),


                        ) {
                        Column(
                           modifier = Modifier.fillMaxWidth(),
                           verticalArrangement = Arrangement.Center,
                           horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                           Text("Empezar", fontSize = 36.sp)
                           Text("Musica", fontSize = 20.sp)
                        }

                        Column(
                           modifier = Modifier
                              .fillMaxSize()
                              .padding(bottom = 16.dp),

                           verticalArrangement = Arrangement.Bottom,
                           horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                           Button(
                              onClick = {
                                 navController.navigate(Destinations.Tasks.route)
                              },
                              modifier = Modifier
                                 .fillMaxWidth()
                                 .padding(start = 16.dp, end = 16.dp)
                           ) {
                              Text("Go!")
                           }
                        }
                     }
                  }
               }
            }
         }
      }

      TabRow(selectedTabIndex = selectedPage) {
         for (index in 0 until pagerState.pageCount) {
            Tab(
               selected = selectedPage == index,
               onClick = { selectedPage = index }
            ) {
               when (index) {
                  0 -> Text("1")
                  1 -> Text("2")
                  2 -> Text("3")
               }
            }
         }
      }
   }
}
