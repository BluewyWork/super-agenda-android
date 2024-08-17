package com.example.superagenda.presentation.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.superagenda.core.navigations.Destinations
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun Navigation(
   navController: NavController,
   topBarTitle: String,
   floatingActionButton: @Composable () -> Unit = {},
   navigationIcon: @Composable () -> Unit = {},
   navigationViewModel: NavigationViewModel,
   content: @Composable (padding: PaddingValues) -> Unit,
) {
   val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
   val scope = rememberCoroutineScope()

   CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
      ModalNavigationDrawer(
         drawerState = drawerState,
         drawerContent = {
            ModalDrawerSheet {
               DrawerContent(
                  navController,
                  scope,
                  drawerState,
                  navigationViewModel
               )
            }
         },
      ) {
         CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {
            Scaffold(
               topBar = {
                  TopBar(scope, drawerState, topBarTitle, navigationIcon)
               },
               bottomBar = { BottomBar(navController) },
               floatingActionButton = floatingActionButton,
            ) { contentPadding ->
               content(contentPadding)
            }
         }
      }
   }
}

@Composable
fun DrawerContent(
   navController: NavController,
   scope: CoroutineScope,
   drawerState: DrawerState,
   navigationViewModel: NavigationViewModel
) {
   Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
      navigationViewModel.onShow()
      val loggedIn: Boolean by navigationViewModel.isLoggedIn.observeAsState(false)

      if (!loggedIn) {
         Button(onClick = {
            navController.navigate(Destinations.Login.route)
            scope.launch { drawerState.close() }
         }, modifier = Modifier.fillMaxWidth()) {
            Text(text = "Authentication")
            Icon(imageVector = Icons.Filled.Lock, contentDescription = "auth")
         }
      } else {
         Button(onClick = {
            navController.navigate(Destinations.Profile.route)
            scope.launch { drawerState.close() }
         }, modifier = Modifier.fillMaxWidth()) {
            Text("Profile")
            Icon(imageVector = Icons.Filled.Person, contentDescription = "profile")
         }
      }
      HorizontalDivider()
   }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
   scope: CoroutineScope,
   drawerState: DrawerState,
   topBarTitle: String,
   navigationIcon: @Composable () -> Unit,
) {
   CenterAlignedTopAppBar(
      title = { Text(topBarTitle) },
      navigationIcon = navigationIcon,
      actions = {
         IconButton(onClick = {
            scope.launch {
               drawerState.apply {
                  if (isClosed) open() else close()
               }
            }
         }) {
            Icon(
               imageVector = Icons.Filled.Menu,
               contentDescription = "Menu"
            )
         }
      }

   )
}

@Composable
fun BottomBar(navController: NavController) {
   androidx.compose.material3.NavigationBar(
      modifier = Modifier
         .height(50.dp),
      containerColor = MaterialTheme.colorScheme.primaryContainer,
      contentColor = MaterialTheme.colorScheme.primary
   ) {
      NavigationBarItem(
         selected = false,
         onClick = { navController.navigate(Destinations.Tasks.route) },
         icon = {
            Icon(
               imageVector = Icons.Outlined.Home,
               contentDescription = "Tasks Overview",
            )
         }
      )
      NavigationBarItem(
         selected = false,
         onClick = { navController.navigate("filter") },
         icon = {
            Text("F")
         }
      )
   }
}