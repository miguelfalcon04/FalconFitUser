package com.example.falconfituser.data.machine

import com.example.falconfituser.data.api.machine.MachineRaw

fun MachineRaw.toExternal(): Machine{
    return Machine(
        id = this.id.toString(),
        title = this.attributes.title,
        subtitle = this.attributes.subtitle,
        description = this.attributes.description,
        photo = this.attributes.photo?.data?.firstOrNull()?.attributes?.formats?.small?.url ?: ""
            //this.attributes.photo.attributes.formats?.small!!.url
    )
}

fun List<MachineRaw>.toExternal():List<Machine> = map(MachineRaw::toExternal)