package com.example.falconfituser.data.api

import android.R

data class MachineApiModel (
    val title:String,
    val subtitle:String,
    val description: String,
)

data class MachinesListApiModel(
    val list: List<MachineApiModel>
)

data class MachineDetailResponse(
    val title: String,
    val subtitle: String,
    val description: String,
){
    fun asApiModel(): MachinesListApiModel {
        return MachinesListApiModel(
            list = listOf(
                MachineApiModel(
                    title = title,
                    subtitle = subtitle,
                    description = description
                )
            )
        )
    }
}



data class MachineListRawResponse(
    val id: Int,
    val title: String,
    val subtitle: String,
    val description: String,
)