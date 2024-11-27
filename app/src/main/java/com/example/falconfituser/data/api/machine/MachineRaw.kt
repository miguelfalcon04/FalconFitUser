package com.example.falconfituser.data.api.machine

class MachineRaw (
    val id: Int,
    val attributes: MachineRawAttributes
)

data class MachineRawAttributes(
    val title: String,
    val subtitle: String,
    val description: String,
)