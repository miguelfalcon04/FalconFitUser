package com.example.falconfituser.data.machine

import com.example.falconfituser.data.api.machine.MachineRaw

fun MachineRaw.toExternal(): Machine{
    return Machine(
        id = this.id.toString(),
        title = this.attributes.title,
        subtitle = this.attributes.subtitle,
        description = this.attributes.description,
    )
}

fun List<MachineRaw>.toExternal():List<Machine> = map(MachineRaw::toExternal)