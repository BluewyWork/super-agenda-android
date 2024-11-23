package com.example.superagenda.data.network

object Endpoints {
   const val REGISTER_USER = "/api/auth/register/user"
   const val LOGIN_USER = "/api/auth/login/user"

   const val NEW_TASK = "/api/claims/tasks"
   const val GET_TASK_LIST = "/api/claims/tasks"
   const val UPDATE_TASK = "/api/claims/tasks"
   const val UPDATE_TASK_LIST = "/api/claims/tasks"
   const val DELETE_TASK = "/api/claims/tasks"

   const val GET_PROFILE = "/api/claims/user"
   const val DELETE_PROFILE = "/api/claims/user"
}
