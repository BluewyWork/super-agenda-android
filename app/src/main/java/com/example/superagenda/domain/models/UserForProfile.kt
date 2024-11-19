package com.example.superagenda.domain.models

data class UserForProfile(
   val username: String,
   val membership: Membership,
)

enum class Membership {
   FREE,
   PREMIUM
}