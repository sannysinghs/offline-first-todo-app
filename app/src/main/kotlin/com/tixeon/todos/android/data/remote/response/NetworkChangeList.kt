package com.tixeon.todos.android.data.remote.response

import com.google.gson.annotations.SerializedName

data class NetworkChangeList(
    @SerializedName("resourceId")
    val resourceId: String,
    @SerializedName("version")
    val version: Int,
    @SerializedName("isDelete")
    val isDelete: Boolean
)
