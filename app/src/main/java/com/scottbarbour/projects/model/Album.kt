package com.scottbarbour.projects.model

import com.google.gson.annotations.SerializedName

data class Album(
    @SerializedName("id") val id: Int,
    @SerializedName("userId") val userId: Int,
    @SerializedName("title") val title: String)
