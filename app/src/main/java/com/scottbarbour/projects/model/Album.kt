package com.scottbarbour.projects.model

import com.google.gson.annotations.SerializedName

data class Album(
    @SerializedName("id") val id: String,
    @SerializedName("userId") val userId: String,
    @SerializedName("title") val title: String)
