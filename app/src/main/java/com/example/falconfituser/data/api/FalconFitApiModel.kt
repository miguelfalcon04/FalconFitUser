package com.example.falconfituser.data.api

import android.R

data class FalconFitApiModel (
    val id: R.string
)

data class MachineListRawResponse(
    val id: Int,
    val title: String,
    val subtitle: String,
    val description: String,
)