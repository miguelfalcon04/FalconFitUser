package com.example.falconfituser.data.remote.machine

class MachineRaw (
    val id: Int,
    val attributes: MachineRawAttributes
)

data class MachineRawAttributes(
    val title: String,
    val subtitle: String,
    val description: String,
    val taken: Boolean
)