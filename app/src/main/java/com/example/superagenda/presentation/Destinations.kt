package com.example.superagenda.presentation

sealed class Destinations(
   val route: String,
) {
   object Slider : Destinations("slider")
   object Register : Destinations("register")
   object Login : Destinations("login")
   object Profile : Destinations("profile")
   object TaskEdit : Destinations("task_edit")
   object NewTask : Destinations("new_task")
   object Filter : Destinations("filter")
   object Tasks : Destinations("tasks")
   object Other : Destinations("other")
}