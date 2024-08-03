package com.example.superagenda.core.navigations

sealed class Destinations(
   val route: String,
) {
   object Register : Destinations("register")
   object Login : Destinations("login")
   object Profile : Destinations("profile")
   object TasksNotStarted : Destinations("tasks_not_started")
   object TasksOngoing : Destinations("tasks_ongoing")
   object TasksCompleted : Destinations("tasks_completed")
   object TaskEdit : Destinations("task_edit")
   object NewTask : Destinations("new_task")
   object NoInternet : Destinations("no_internet")
   object TasksOverview : Destinations("tasks_overview")
   object Filter : Destinations("filter")
   object Tasks : Destinations("tasks")
}