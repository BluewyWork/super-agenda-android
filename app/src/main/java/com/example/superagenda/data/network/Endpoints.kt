package com.example.superagenda.data.network

object Endpoints {
    const val REGISTER_USER = "/api/user/auth/register"
    const val LOGIN_USER = "/api/user/auth/login"
    const val NEW_TASK = "/api/user/task/create"
    const val GET_TASK_LIST = "/api/user/task/show"
    const val UPDATE_TASK = "/api/user/task/update"
    const val UPDATE_TASK_LIST = "/api/user/task/update/list"
    const val GET_PROFILE = "/api/user/self/show"
    const val DELETE_PROFILE = "/api/user/self/delete"
}