package com.example.superagenda.presentation.composables

import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun NavigationBar(navController: NavController) {
    NavigationBar(
        modifier = Modifier
            .height(50.dp),
        containerColor = MaterialTheme.colorScheme.primaryContainer,
        contentColor = MaterialTheme.colorScheme.primary
    ) {
        NavigationBarItem(
            selected = false,
            onClick = { navController.navigate("new_task") },
            icon = {
                Icon(
                    imageVector = Icons.Outlined.Add,
                    contentDescription = "New Task",
                )
            }
        )
        NavigationBarItem(
            selected = false,
            onClick = { navController.navigate("tasks_not_started") },
            icon = {
                Icon(
                    imageVector = Icons.Outlined.Home,
                    contentDescription = "Home",
                )
            }
        )
        NavigationBarItem(
            selected = false,
            onClick = { navController.navigate("tasks_ongoing") },
            icon = {
                Icon(
                    imageVector = Icons.Outlined.ShoppingCart,
                    contentDescription = "Home",
                )
            }
        )
        NavigationBarItem(
            selected = false,
            onClick = { navController.navigate("tasks_completed") },
            icon = {
                Icon(
                    imageVector = Icons.Outlined.Check,
                    contentDescription = "Home",
                )
            }
        )
        NavigationBarItem(
            selected = false,
            onClick = { navController.navigate("profile") },
            icon = {
                Icon(
                    imageVector = Icons.Outlined.Person,
                    contentDescription = "Person",
                )
            }
        )
    }
}
