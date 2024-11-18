package com.example.superagenda.data.models

import com.example.superagenda.data.database.entities.UserForProfileEntity
import com.example.superagenda.domain.models.Membership
import com.example.superagenda.domain.models.UserForProfile
import com.google.gson.annotations.SerializedName

data class UserForProfileModel(
   @SerializedName("username") val username: String,
   @SerializedName("membership") val membership: MembershipModel
)

enum class MembershipModel {
   FREE,
   PREMIUM
}

fun UserForProfile.toData() = UserForProfileModel(
   username = username,

   membership = when (membership) {
      Membership.FREE -> MembershipModel.FREE
      Membership.PREMIUM -> MembershipModel.PREMIUM
   }
)

fun UserForProfileModel.toDomain() = UserForProfile(
   username = username,

   membership = when (membership) {
      MembershipModel.FREE -> Membership.FREE
      MembershipModel.PREMIUM -> Membership.PREMIUM
   }
)

fun UserForProfileModel.toDatabase() = UserForProfileEntity(
   username= username,
   membership = membership
)

fun UserForProfileEntity.toData() = UserForProfileModel(
   username = username,
   membership = membership,
)