package com.example.superagenda.presentation.screens.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.superagenda.domain.models.UserForProfile
import com.example.superagenda.presentation.composables.NavigationBar
import com.example.superagenda.presentation.screens.profile.composables.UsernameTextField

@Composable
fun ProfileScreen(profileViewModel: ProfileViewModel, navController: NavController) {
    Scaffold(bottomBar = { NavigationBar(navController = navController) }) { innerPadding ->
        Column(
            Modifier
                .padding(innerPadding)
        ) {
            Text(text = "Your Profile:")
            Profile(profileViewModel)
        }
    }
}

@Composable
fun Profile(profileViewModel: ProfileViewModel) {
    val userForProfile: UserForProfile? by profileViewModel.userForProfile.observeAsState()

    LazyColumn(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally

    ) {
        item {
            userForProfile?.let {
                UsernameTextField(username = it.username) {}
            }
        }
    }
}