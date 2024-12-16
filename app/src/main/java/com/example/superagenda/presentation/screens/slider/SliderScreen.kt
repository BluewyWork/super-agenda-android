package com.example.superagenda.presentation.screens.slider

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Button
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun SliderScreen(sliderScreenViewModel: SliderScreenViewModel, navController: NavController) {
   Slider(sliderScreenViewModel, navController)
}

@Composable
fun Slider(sliderScreenViewModel: SliderScreenViewModel, navController: NavController) {
   val scope = rememberCoroutineScope()
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
                  PageFirst(pagerState, scope, sliderScreenViewModel, navController)
               }

               1 -> {
                  PageSecond(pagerState, scope, sliderScreenViewModel, navController)
               }

               2 -> {
                  PageThird(sliderScreenViewModel, navController)
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

@Composable
fun PageFirst(
   pagerState: PagerState,
   scope: CoroutineScope,
   sliderScreenViewModel: SliderScreenViewModel,
   navController: NavController,
) {
   Box(modifier = Modifier.fillMaxSize()) {
      Image(
         painter = painterResource(id = R.drawable.one),
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
            Text("Welcome", fontSize = 26.sp)
            Spacer(Modifier.height(10.dp))
            Text("A simple way to manage your tasks.", fontSize = 20.sp)
         }

         Column(
            modifier = Modifier
               .fillMaxSize()
               .padding(bottom = 16.dp),

            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.CenterHorizontally
         ) {
            Row {
               Button(
                  onClick = {
                     sliderScreenViewModel.saveShownTrue()
                     navController.navigate(Destinations.Tasks.route)
                  },

                  modifier = Modifier
                     .fillMaxWidth()
                     .padding(start = 16.dp, end = 16.dp)
                     .weight(0.5f)
               ) {
                  Text("Skip")
               }

               Button(
                  onClick = {
                     scope.launch {
                        pagerState.scrollToPage(1)
                     }
                  },
                  modifier = Modifier
                     .fillMaxWidth()
                     .weight(0.5f)
                     .padding(start = 16.dp, end = 16.dp)
               ) {
                  Text("Next")
               }

            }

         }
      }
   }
}

@Composable
fun PageSecond(
   pagerState: PagerState,
   scope: CoroutineScope,
   sliderScreenViewModel: SliderScreenViewModel,
   navController: NavController,
) {
   Box(modifier = Modifier.fillMaxSize()) {
      Image(
         painter = painterResource(id = R.drawable.sixth),
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
            Text("Disfruta las tareas!", fontSize = 36.sp)
         }

         Column(
            modifier = Modifier
               .fillMaxSize()
               .padding(bottom = 16.dp),

            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.CenterHorizontally
         ) {
            Row {
               Button(
                  onClick = {
                     sliderScreenViewModel.saveShownTrue()
                     navController.navigate(Destinations.Tasks.route)
                  },

                  modifier = Modifier
                     .fillMaxWidth()
                     .padding(start = 16.dp, end = 16.dp)
                     .weight(.5f)
               ) {
                  Text("Skip")
               }

               Button(
                  onClick = {
                     scope.launch {
                        pagerState.scrollToPage(2)
                     }
                  },

                  modifier = Modifier
                     .fillMaxWidth()
                     .padding(start = 16.dp, end = 16.dp)
                     .weight(.5f)
               ) {
                  Text("Next")
               }
            }
         }
      }
   }
}

@Composable
fun PageThird(sliderScreenViewModel: SliderScreenViewModel, navController: NavController) {
   Box(modifier = Modifier.fillMaxSize()) {
      Image(
         painter = painterResource(id = R.drawable.fifth),
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
            Text("...", fontSize = 20.sp)
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
                  sliderScreenViewModel.saveShownTrue()
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