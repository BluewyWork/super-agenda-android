package com.example.superagenda.core.navigations

sealed class Destinations(
    val route: String
) {
    object Profile : Destinations("profile")
    object Tasks : Destinations("tasks")
}