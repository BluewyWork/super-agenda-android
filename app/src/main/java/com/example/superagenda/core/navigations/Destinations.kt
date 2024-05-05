package com.example.superagenda.core.navigations

sealed class Destinations(
    val route: String
) {
    object Profile : Destinations("profile")
    object Tasks : Destinations("tasks")
    object TasksNotStarted : Destinations("tasks_not_started")
    object TasksOngoing : Destinations("tasks_ongoing")
    object TasksCompleted : Destinations("tasks_completed")
}