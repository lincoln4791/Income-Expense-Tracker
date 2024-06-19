package com.lincoln4791.dailyexpensemanager.model

import java.io.Serializable

data class Profile(
    val name : String,
    val phone : String,
    val email : String,
    val profilePicUri : String
) : Serializable {
}